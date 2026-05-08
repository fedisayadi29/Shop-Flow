package com.shopflow.service;

import com.shopflow.dto.request.LoginRequest;
import com.shopflow.dto.request.RefreshTokenRequest;
import com.shopflow.dto.request.RegisterRequest;
import com.shopflow.dto.response.AuthResponse;
import com.shopflow.dto.response.SellerProfileResponse;
import com.shopflow.dto.response.UserResponse;
import com.shopflow.entity.*;
import com.shopflow.exception.ShopFlowException;
import com.shopflow.repository.RefreshTokenRepository;
import com.shopflow.repository.SellerProfileRepository;
import com.shopflow.repository.UserRepository;
import com.shopflow.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final SellerProfileRepository sellerProfileRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, SellerProfileRepository sellerProfileRepository,
                       RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.sellerProfileRepository = sellerProfileRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw ShopFlowException.conflict("Un compte avec cet email existe déjà");
        }

        User user = User.builder()
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .prenom(request.getPrenom())
                .nom(request.getNom())
                .role(request.getRole() != null ? request.getRole() : Role.CUSTOMER)
                .actif(true)
                .build();

        user = userRepository.save(user);

        // Créer le profil vendeur si nécessaire
        if (user.getRole() == Role.SELLER) {
            if (request.getNomBoutique() == null || request.getNomBoutique().isBlank()) {
                throw ShopFlowException.badRequest("Le nom de la boutique est obligatoire pour un vendeur");
            }
            SellerProfile profile = SellerProfile.builder()
                    .user(user)
                    .nomBoutique(request.getNomBoutique())
                    .description(request.getDescriptionBoutique())
                    .logo(request.getLogo())
                    .build();
            sellerProfileRepository.save(profile);
        }

        // Créer le panier pour les clients
        if (user.getRole() == Role.CUSTOMER) {
            Cart cart = Cart.builder().customer(user).build();
            // cart will be saved via cascade or CartService
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = saveRefreshToken(user);

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> ShopFlowException.notFound("Utilisateur non trouvé"));

        // Révoquer les anciens refresh tokens
        refreshTokenRepository.revokeAllUserTokens(user);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = saveRefreshToken(user);

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> ShopFlowException.unauthorized("Refresh token invalide"));

        if (storedToken.isRevoked()) {
            throw ShopFlowException.unauthorized("Refresh token révoqué");
        }

        if (storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw ShopFlowException.unauthorized("Refresh token expiré");
        }

        User user = storedToken.getUser();
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = saveRefreshToken(user);

        return buildAuthResponse(user, newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    private String saveRefreshToken(User user) {
        String tokenValue = jwtService.generateRefreshToken(user);
        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenValue)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenValue;
    }

    private AuthResponse buildAuthResponse(User user, String accessToken, String refreshToken) {
        SellerProfileResponse sellerProfileResponse = null;
        if (user.getRole() == Role.SELLER) {
            sellerProfileRepository.findByUser(user).ifPresent(profile -> {
                // handled below
            });
            var profileOpt = sellerProfileRepository.findByUser(user);
            if (profileOpt.isPresent()) {
                var p = profileOpt.get();
                sellerProfileResponse = SellerProfileResponse.builder()
                        .id(p.getId())
                        .nomBoutique(p.getNomBoutique())
                        .description(p.getDescription())
                        .logo(p.getLogo())
                        .note(p.getNote())
                        .build();
            }
        }

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .prenom(user.getPrenom())
                .nom(user.getNom())
                .role(user.getRole())
                .actif(user.isActif())
                .dateCreation(user.getDateCreation())
                .sellerProfile(sellerProfileResponse)
                .build();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getJwtExpiration())
                .user(userResponse)
                .build();
    }
}

package com.shopflow.service;

import com.shopflow.dto.request.AddressRequest;
import com.shopflow.dto.response.AddressResponse;
import com.shopflow.dto.response.UserResponse;
import com.shopflow.entity.Address;
import com.shopflow.entity.User;
import com.shopflow.exception.ShopFlowException;
import com.shopflow.repository.AddressRepository;
import com.shopflow.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toUserResponse);
    }

    @Transactional
    public UserResponse toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ShopFlowException.notFound("Utilisateur non trouvé : " + id));
        user.setActif(!user.isActif());
        user = userRepository.save(user);
        return toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses(User user) {
        return addressRepository.findByUser(user).stream()
                .map(this::toAddressResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressResponse addAddress(User user, AddressRequest request) {
        // Si c'est la première adresse ou marquée principale, désactiver les autres
        if (request.isPrincipal()) {
            addressRepository.findByUser(user).forEach(addr -> {
                addr.setPrincipal(false);
                addressRepository.save(addr);
            });
        }

        Address address = Address.builder()
                .user(user)
                .rue(request.getRue())
                .ville(request.getVille())
                .codePostal(request.getCodePostal())
                .pays(request.getPays())
                .principal(request.isPrincipal())
                .build();

        address = addressRepository.save(address);
        return toAddressResponse(address);
    }

    @Transactional
    public void deleteAddress(User user, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> ShopFlowException.notFound("Adresse non trouvée"));
        if (!address.getUser().getId().equals(user.getId())) {
            throw ShopFlowException.forbidden("Cette adresse ne vous appartient pas");
        }
        addressRepository.delete(address);
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .prenom(user.getPrenom())
                .nom(user.getNom())
                .role(user.getRole())
                .actif(user.isActif())
                .dateCreation(user.getDateCreation())
                .build();
    }

    private AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .rue(address.getRue())
                .ville(address.getVille())
                .codePostal(address.getCodePostal())
                .pays(address.getPays())
                .principal(address.isPrincipal())
                .build();
    }
}

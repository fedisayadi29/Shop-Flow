package com.shopflow.service;

import com.shopflow.dto.response.*;
import com.shopflow.entity.OrderStatus;
import com.shopflow.entity.Role;
import com.shopflow.entity.User;
import com.shopflow.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final OrderService orderService;

    public DashboardService(OrderRepository orderRepository, ProductRepository productRepository,
                            UserRepository userRepository, ProductService productService,
                            OrderService orderService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productService = productService;
        this.orderService = orderService;
    }

    @Transactional(readOnly = true)
    public DashboardAdminResponse getAdminDashboard() {
        var topProduits = productRepository.findTopSelling(PageRequest.of(0, 5))
                .stream().map(productService::toProductResponse).collect(Collectors.toList());

        var commandesRecentes = orderRepository.findTop10ByOrderByDateCommandeDesc()
                .stream().map(orderService::toOrderResponse).collect(Collectors.toList());

        var topVendeurs = userRepository.findByRole(Role.SELLER, PageRequest.of(0, 5))
                .stream().map(u -> {
                    String nomBoutique = u.getSellerProfile() != null ? u.getSellerProfile().getNomBoutique() : null;
                    String logo = u.getSellerProfile() != null ? u.getSellerProfile().getLogo() : null;
                    return SellerSummaryResponse.builder()
                            .id(u.getId())
                            .prenom(u.getPrenom())
                            .nom(u.getNom())
                            .nomBoutique(nomBoutique)
                            .logo(logo)
                            .build();
                }).collect(Collectors.toList());

        // Compter uniquement les produits actifs
        long totalProduitsActifs = productRepository.countByActifTrue();

        return DashboardAdminResponse.builder()
                .chiffreAffairesGlobal(orderRepository.calculateChiffreAffairesGlobal())
                .totalCommandes(orderRepository.count())
                .totalUtilisateurs(userRepository.count())
                .totalProduits(totalProduitsActifs)
                .topProduits(topProduits)
                .topVendeurs(topVendeurs)
                .commandesRecentes(commandesRecentes)
                .build();
    }

    @Transactional(readOnly = true)
    public DashboardSellerResponse getSellerDashboard(User seller) {
        var produitsStockFaible = productRepository.findLowStockBySeller(seller, 5)
                .stream().map(productService::toProductResponse).collect(Collectors.toList());

        var commandesRecentes = orderRepository.findBySellerProducts(
                seller, PageRequest.of(0, 10, org.springframework.data.domain.Sort.by("dateCommande").descending()))
                .stream().map(orderService::toOrderResponse).collect(Collectors.toList());

        long totalProduits = productRepository.findBySeller(seller, PageRequest.of(0, Integer.MAX_VALUE)).getTotalElements();

        return DashboardSellerResponse.builder()
                .revenus(orderRepository.calculateRevenusBySeller(seller))
                .commandesEnAttente(orderRepository.countCommandesEnAttenteBySeller(seller))
                .totalProduits(totalProduits)
                .alertesStockFaible(productRepository.countLowStockBySeller(seller, 5))
                .produitsStockFaible(produitsStockFaible)
                .commandesRecentes(commandesRecentes)
                .build();
    }
}

package com.shopflow.controller;

import com.shopflow.dto.request.OrderRequest;
import com.shopflow.dto.response.OrderResponse;
import com.shopflow.dto.response.PageResponse;
import com.shopflow.entity.OrderStatus;
import com.shopflow.entity.User;
import com.shopflow.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Commandes", description = "Gestion des commandes")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Passer une commande depuis le panier")
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal User customer,
            @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(customer, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Détail d'une commande")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(orderService.getOrderById(id, currentUser));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Commandes du client connecté")
    public ResponseEntity<PageResponse<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal User customer,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getMyOrders(customer, page, size));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Operation(summary = "Toutes les commandes (ADMIN) ou commandes du vendeur (SELLER)")
    public ResponseEntity<PageResponse<OrderResponse>> getAllOrders(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) OrderStatus statut) {
        // Si c'est un vendeur, forcer le filtre sur son ID
        Long filterSellerId = currentUser.getRole() == com.shopflow.entity.Role.SELLER 
            ? currentUser.getId() 
            : sellerId;
        return ResponseEntity.ok(orderService.getAllOrders(page, size, filterSellerId, statut));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @Operation(summary = "Mettre à jour le statut (SELLER/ADMIN)")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(orderService.updateStatus(id, request.getStatut(), currentUser));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Annuler une commande (CUSTOMER si éligible)")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal User customer) {
        return ResponseEntity.ok(orderService.cancelOrder(id, customer));
    }

    static class StatusUpdateRequest {
        private OrderStatus statut;
        public OrderStatus getStatut() { return statut; }
        public void setStatut(OrderStatus statut) { this.statut = statut; }
    }
}

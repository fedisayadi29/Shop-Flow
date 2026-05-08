package com.shopflow.controller;

import com.shopflow.dto.request.CartItemRequest;
import com.shopflow.dto.response.CartResponse;
import com.shopflow.entity.User;
import com.shopflow.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('CUSTOMER')")
@Tag(name = "Panier", description = "Gestion du panier d'achat")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @Operation(summary = "Panier du client connecté")
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal User customer) {
        return ResponseEntity.ok(cartService.getOrCreateCart(customer));
    }

    @PostMapping("/items")
    @Operation(summary = "Ajouter un article au panier")
    public ResponseEntity<CartResponse> addItem(
            @AuthenticationPrincipal User customer,
            @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(customer, request));
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Modifier la quantité d'un article")
    public ResponseEntity<CartResponse> updateItem(
            @AuthenticationPrincipal User customer,
            @PathVariable Long itemId,
            @RequestBody UpdateQuantityRequest request) {
        return ResponseEntity.ok(cartService.updateItem(customer, itemId, request.getQuantite()));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Retirer un article du panier")
    public ResponseEntity<CartResponse> removeItem(
            @AuthenticationPrincipal User customer,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItem(customer, itemId));
    }

    @PostMapping("/coupon")
    @Operation(summary = "Appliquer un code promo")
    public ResponseEntity<CartResponse> applyCoupon(
            @AuthenticationPrincipal User customer,
            @RequestBody CouponCodeRequest request) {
        return ResponseEntity.ok(cartService.applyCoupon(customer, request.getCode()));
    }

    @DeleteMapping("/coupon")
    @Operation(summary = "Retirer le code promo")
    public ResponseEntity<CartResponse> removeCoupon(@AuthenticationPrincipal User customer) {
        return ResponseEntity.ok(cartService.removeCoupon(customer));
    }

    static class UpdateQuantityRequest {
        private Integer quantite;
        public Integer getQuantite() { return quantite; }
        public void setQuantite(Integer quantite) { this.quantite = quantite; }
    }

    static class CouponCodeRequest {
        private String code;
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }
}

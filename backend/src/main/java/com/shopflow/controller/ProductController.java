package com.shopflow.controller;

import com.shopflow.dto.request.ProductRequest;
import com.shopflow.dto.response.PageResponse;
import com.shopflow.dto.response.ProductResponse;
import com.shopflow.entity.Role;
import com.shopflow.entity.User;
import com.shopflow.exception.ShopFlowException;
import com.shopflow.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Produits", description = "Gestion du catalogue produits")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Liste paginée des produits avec filtres")
    public ResponseEntity<PageResponse<ProductResponse>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal prixMin,
            @RequestParam(required = false) BigDecimal prixMax,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) Boolean promo,
            @RequestParam(defaultValue = "nouveautes") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(productService.getProducts(categoryId, prixMin, prixMax, sellerId, promo, sortBy, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'un produit avec variantes et avis")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @Operation(summary = "Créer un produit (SELLER/ADMIN)")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request, currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @Operation(summary = "Modifier un produit")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(productService.updateProduct(id, request, currentUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @Operation(summary = "Désactiver un produit (soft delete)")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        productService.deleteProduct(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche full-text")
    public ResponseEntity<PageResponse<ProductResponse>> searchProducts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(productService.searchProducts(q, page, size));
    }

    @GetMapping("/top-selling")
    @Operation(summary = "Top 10 meilleures ventes")
    public ResponseEntity<List<ProductResponse>> getTopSelling() {
        return ResponseEntity.ok(productService.getTopSelling());
    }
}

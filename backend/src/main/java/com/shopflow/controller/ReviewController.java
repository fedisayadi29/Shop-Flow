package com.shopflow.controller;

import com.shopflow.dto.request.ReviewRequest;
import com.shopflow.dto.response.PageResponse;
import com.shopflow.dto.response.ReviewResponse;
import com.shopflow.entity.User;
import com.shopflow.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Avis", description = "Gestion des avis produits")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Poster un avis (achat vérifié)")
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal User customer,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(customer, request));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Avis d'un produit")
    public ResponseEntity<PageResponse<ReviewResponse>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId, page, size));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Approuver un avis (ADMIN)")
    public ResponseEntity<ReviewResponse> approveReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.approveReview(id));
    }
}

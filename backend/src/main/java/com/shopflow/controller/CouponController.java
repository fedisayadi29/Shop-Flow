package com.shopflow.controller;

import com.shopflow.dto.request.CouponRequest;
import com.shopflow.dto.response.CouponResponse;
import com.shopflow.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupons", description = "Gestion des codes promo")
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Liste des coupons (ADMIN)")
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Créer un coupon (ADMIN)")
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CouponRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(couponService.createCoupon(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Modifier un coupon (ADMIN)")
    public ResponseEntity<CouponResponse> updateCoupon(
            @PathVariable Long id,
            @Valid @RequestBody CouponRequest request) {
        return ResponseEntity.ok(couponService.updateCoupon(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Désactiver un coupon (ADMIN)")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validate/{code}")
    @Operation(summary = "Vérifier la validité d'un code promo")
    public ResponseEntity<CouponResponse> validateCoupon(@PathVariable String code) {
        return ResponseEntity.ok(couponService.validateCoupon(code));
    }
}

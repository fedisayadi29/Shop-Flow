package com.shopflow.controller;

import com.shopflow.dto.response.DashboardAdminResponse;
import com.shopflow.dto.response.DashboardSellerResponse;
import com.shopflow.entity.User;
import com.shopflow.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Tableaux de bord")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Stats globales (ADMIN)")
    public ResponseEntity<DashboardAdminResponse> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    @GetMapping("/seller")
    @PreAuthorize("hasRole('SELLER')")
    @Operation(summary = "Stats du vendeur connecté")
    public ResponseEntity<DashboardSellerResponse> getSellerDashboard(
            @AuthenticationPrincipal User seller) {
        return ResponseEntity.ok(dashboardService.getSellerDashboard(seller));
    }
}

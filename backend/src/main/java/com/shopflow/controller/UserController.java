package com.shopflow.controller;

import com.shopflow.dto.request.AddressRequest;
import com.shopflow.dto.response.AddressResponse;
import com.shopflow.dto.response.UserResponse;
import com.shopflow.entity.User;
import com.shopflow.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Liste des utilisateurs (ADMIN)")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(userService.getAllUsers(PageRequest.of(page, size)));
    }

    @GetMapping("/me")
    @Operation(summary = "Profil de l'utilisateur connecté")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.toUserResponse(currentUser));
    }

    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activer/désactiver un compte (ADMIN)")
    public ResponseEntity<UserResponse> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleUserStatus(id));
    }

    @GetMapping("/me/addresses")
    @Operation(summary = "Adresses de livraison du client")
    public ResponseEntity<List<AddressResponse>> getAddresses(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.getAddresses(currentUser));
    }

    @PostMapping("/me/addresses")
    @Operation(summary = "Ajouter une adresse")
    public ResponseEntity<AddressResponse> addAddress(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addAddress(currentUser, request));
    }

    @DeleteMapping("/me/addresses/{addressId}")
    @Operation(summary = "Supprimer une adresse")
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long addressId) {
        userService.deleteAddress(currentUser, addressId);
        return ResponseEntity.noContent().build();
    }
}

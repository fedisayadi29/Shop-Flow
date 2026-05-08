package com.shopflow.dto.response;

import com.shopflow.entity.Role;
import java.time.LocalDateTime;

public class UserResponse {

    private Long id; private String email; private String prenom; private String nom;
    private Role role; private boolean actif; private LocalDateTime dateCreation;
    private SellerProfileResponse sellerProfile;

    public UserResponse() {}

    public UserResponse(Long id, String email, String prenom, String nom, Role role,
                        boolean actif, LocalDateTime dateCreation, SellerProfileResponse sellerProfile) {
        this.id = id; this.email = email; this.prenom = prenom; this.nom = nom;
        this.role = role; this.actif = actif; this.dateCreation = dateCreation;
        this.sellerProfile = sellerProfile;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public SellerProfileResponse getSellerProfile() { return sellerProfile; }
    public void setSellerProfile(SellerProfileResponse sellerProfile) { this.sellerProfile = sellerProfile; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String email; private String prenom; private String nom;
        private Role role; private boolean actif; private LocalDateTime dateCreation;
        private SellerProfileResponse sellerProfile;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder prenom(String prenom) { this.prenom = prenom; return this; }
        public Builder nom(String nom) { this.nom = nom; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder actif(boolean actif) { this.actif = actif; return this; }
        public Builder dateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; return this; }
        public Builder sellerProfile(SellerProfileResponse sellerProfile) { this.sellerProfile = sellerProfile; return this; }

        public UserResponse build() { return new UserResponse(id, email, prenom, nom, role, actif, dateCreation, sellerProfile); }
    }
}

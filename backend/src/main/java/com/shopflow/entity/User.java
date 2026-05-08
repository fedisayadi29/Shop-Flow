package com.shopflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String motDePasse;

    @NotBlank
    private String prenom;

    @NotBlank
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean actif = true;

    @Column(updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExpiry;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SellerProfile sellerProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> addresses;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart cart;

    public User() {
        this.actif = true;
        this.dateCreation = LocalDateTime.now();
    }

    public User(Long id, String email, String motDePasse, String prenom, String nom, Role role,
                boolean actif, LocalDateTime dateCreation, String resetPasswordToken,
                LocalDateTime resetPasswordTokenExpiry, SellerProfile sellerProfile,
                List<Address> addresses, Cart cart) {
        this.id = id;
        this.email = email;
        this.motDePasse = motDePasse;
        this.prenom = prenom;
        this.nom = nom;
        this.role = role;
        this.actif = actif;
        this.dateCreation = dateCreation;
        this.resetPasswordToken = resetPasswordToken;
        this.resetPasswordTokenExpiry = resetPasswordTokenExpiry;
        this.sellerProfile = sellerProfile;
        this.addresses = addresses;
        this.cart = cart;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String email;
        private String motDePasse;
        private String prenom;
        private String nom;
        private Role role;
        private boolean actif = true;
        private LocalDateTime dateCreation = LocalDateTime.now();
        private String resetPasswordToken;
        private LocalDateTime resetPasswordTokenExpiry;
        private SellerProfile sellerProfile;
        private List<Address> addresses;
        private Cart cart;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder motDePasse(String motDePasse) { this.motDePasse = motDePasse; return this; }
        public Builder prenom(String prenom) { this.prenom = prenom; return this; }
        public Builder nom(String nom) { this.nom = nom; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder actif(boolean actif) { this.actif = actif; return this; }
        public Builder dateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; return this; }
        public Builder resetPasswordToken(String t) { this.resetPasswordToken = t; return this; }
        public Builder resetPasswordTokenExpiry(LocalDateTime t) { this.resetPasswordTokenExpiry = t; return this; }
        public Builder sellerProfile(SellerProfile sp) { this.sellerProfile = sp; return this; }
        public Builder addresses(List<Address> addresses) { this.addresses = addresses; return this; }
        public Builder cart(Cart cart) { this.cart = cart; return this; }

        public User build() {
            return new User(id, email, motDePasse, prenom, nom, role, actif, dateCreation,
                    resetPasswordToken, resetPasswordTokenExpiry, sellerProfile, addresses, cart);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public String getPassword() { return motDePasse; }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return actif; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return actif; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
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
    public String getResetPasswordToken() { return resetPasswordToken; }
    public void setResetPasswordToken(String resetPasswordToken) { this.resetPasswordToken = resetPasswordToken; }
    public LocalDateTime getResetPasswordTokenExpiry() { return resetPasswordTokenExpiry; }
    public void setResetPasswordTokenExpiry(LocalDateTime t) { this.resetPasswordTokenExpiry = t; }
    public SellerProfile getSellerProfile() { return sellerProfile; }
    public void setSellerProfile(SellerProfile sellerProfile) { this.sellerProfile = sellerProfile; }
    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((User) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() { return "User{id=" + id + ", email='" + email + "', role=" + role + "}"; }
}

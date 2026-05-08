package com.shopflow.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "seller_profiles")
public class SellerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String nomBoutique;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String logo;
    private Double note = 0.0;

    public SellerProfile() { this.note = 0.0; }

    public SellerProfile(Long id, User user, String nomBoutique, String description, String logo, Double note) {
        this.id = id; this.user = user; this.nomBoutique = nomBoutique;
        this.description = description; this.logo = logo; this.note = note;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private User user; private String nomBoutique;
        private String description; private String logo; private Double note = 0.0;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder nomBoutique(String nomBoutique) { this.nomBoutique = nomBoutique; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder logo(String logo) { this.logo = logo; return this; }
        public Builder note(Double note) { this.note = note; return this; }

        public SellerProfile build() { return new SellerProfile(id, user, nomBoutique, description, logo, note); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getNomBoutique() { return nomBoutique; }
    public void setNomBoutique(String nomBoutique) { this.nomBoutique = nomBoutique; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public Double getNote() { return note; }
    public void setNote(Double note) { this.note = note; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((SellerProfile) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

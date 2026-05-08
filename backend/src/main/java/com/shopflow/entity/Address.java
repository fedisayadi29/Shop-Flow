package com.shopflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    private String rue;

    @NotBlank
    private String ville;

    @NotBlank
    private String codePostal;

    @NotBlank
    private String pays;

    private boolean principal = false;

    public Address() { this.principal = false; }

    public Address(Long id, User user, String rue, String ville, String codePostal, String pays, boolean principal) {
        this.id = id; this.user = user; this.rue = rue; this.ville = ville;
        this.codePostal = codePostal; this.pays = pays; this.principal = principal;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private User user; private String rue; private String ville;
        private String codePostal; private String pays; private boolean principal = false;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder rue(String rue) { this.rue = rue; return this; }
        public Builder ville(String ville) { this.ville = ville; return this; }
        public Builder codePostal(String codePostal) { this.codePostal = codePostal; return this; }
        public Builder pays(String pays) { this.pays = pays; return this; }
        public Builder principal(boolean principal) { this.principal = principal; return this; }

        public Address build() { return new Address(id, user, rue, ville, codePostal, pays, principal); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getRue() { return rue; }
    public void setRue(String rue) { this.rue = rue; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    public boolean isPrincipal() { return principal; }
    public void setPrincipal(boolean principal) { this.principal = principal; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((Address) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

package com.shopflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> sousCategories = new ArrayList<>();

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    public Category() {
        this.sousCategories = new ArrayList<>();
        this.products = new ArrayList<>();
    }

    public Category(Long id, String nom, String description, Category parent,
                    List<Category> sousCategories, List<Product> products) {
        this.id = id; this.nom = nom; this.description = description; this.parent = parent;
        this.sousCategories = sousCategories; this.products = products;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String nom; private String description; private Category parent;
        private List<Category> sousCategories = new ArrayList<>();
        private List<Product> products = new ArrayList<>();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder nom(String nom) { this.nom = nom; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder parent(Category parent) { this.parent = parent; return this; }
        public Builder sousCategories(List<Category> s) { this.sousCategories = s; return this; }
        public Builder products(List<Product> products) { this.products = products; return this; }

        public Category build() { return new Category(id, nom, description, parent, sousCategories, products); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Category getParent() { return parent; }
    public void setParent(Category parent) { this.parent = parent; }
    public List<Category> getSousCategories() { return sousCategories; }
    public void setSousCategories(List<Category> sousCategories) { this.sousCategories = sousCategories; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((Category) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() { return "Category{id=" + id + ", nom='" + nom + "'}"; }
}

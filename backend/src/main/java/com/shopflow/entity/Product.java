package com.shopflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @NotBlank
    @Column(nullable = false)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(precision = 10, scale = 2)
    private BigDecimal prixPromo;

    private Integer stock = 0;
    private boolean actif = true;

    @Column(updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    private Integer nombreVentes = 0;
    private Double noteMoyenne = 0.0;
    private Integer nombreAvis = 0;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_categories",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductVariant> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    public Product() {
        this.stock = 0; this.actif = true; this.dateCreation = LocalDateTime.now();
        this.nombreVentes = 0; this.noteMoyenne = 0.0; this.nombreAvis = 0;
        this.images = new ArrayList<>(); this.categories = new ArrayList<>();
        this.variants = new ArrayList<>(); this.reviews = new ArrayList<>();
    }

    public Product(Long id, User seller, String nom, String description, BigDecimal prix,
                   BigDecimal prixPromo, Integer stock, boolean actif, LocalDateTime dateCreation,
                   Integer nombreVentes, Double noteMoyenne, Integer nombreAvis,
                   List<String> images, List<Category> categories,
                   List<ProductVariant> variants, List<Review> reviews) {
        this.id = id; this.seller = seller; this.nom = nom; this.description = description;
        this.prix = prix; this.prixPromo = prixPromo; this.stock = stock; this.actif = actif;
        this.dateCreation = dateCreation; this.nombreVentes = nombreVentes;
        this.noteMoyenne = noteMoyenne; this.nombreAvis = nombreAvis;
        this.images = images; this.categories = categories;
        this.variants = variants; this.reviews = reviews;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private User seller; private String nom; private String description;
        private BigDecimal prix; private BigDecimal prixPromo; private Integer stock = 0;
        private boolean actif = true; private LocalDateTime dateCreation = LocalDateTime.now();
        private Integer nombreVentes = 0; private Double noteMoyenne = 0.0; private Integer nombreAvis = 0;
        private List<String> images = new ArrayList<>(); private List<Category> categories = new ArrayList<>();
        private List<ProductVariant> variants = new ArrayList<>(); private List<Review> reviews = new ArrayList<>();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder seller(User seller) { this.seller = seller; return this; }
        public Builder nom(String nom) { this.nom = nom; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder prix(BigDecimal prix) { this.prix = prix; return this; }
        public Builder prixPromo(BigDecimal prixPromo) { this.prixPromo = prixPromo; return this; }
        public Builder stock(Integer stock) { this.stock = stock; return this; }
        public Builder actif(boolean actif) { this.actif = actif; return this; }
        public Builder dateCreation(LocalDateTime d) { this.dateCreation = d; return this; }
        public Builder nombreVentes(Integer n) { this.nombreVentes = n; return this; }
        public Builder noteMoyenne(Double n) { this.noteMoyenne = n; return this; }
        public Builder nombreAvis(Integer n) { this.nombreAvis = n; return this; }
        public Builder images(List<String> images) { this.images = images; return this; }
        public Builder categories(List<Category> categories) { this.categories = categories; return this; }
        public Builder variants(List<ProductVariant> variants) { this.variants = variants; return this; }
        public Builder reviews(List<Review> reviews) { this.reviews = reviews; return this; }

        public Product build() {
            return new Product(id, seller, nom, description, prix, prixPromo, stock, actif,
                    dateCreation, nombreVentes, noteMoyenne, nombreAvis, images, categories, variants, reviews);
        }
    }

    public boolean isEnPromotion() {
        return prixPromo != null && prixPromo.compareTo(BigDecimal.ZERO) > 0 && prixPromo.compareTo(prix) < 0;
    }

    public Double getPourcentageRemise() {
        if (!isEnPromotion()) return 0.0;
        return (1 - prixPromo.doubleValue() / prix.doubleValue()) * 100;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }
    public BigDecimal getPrixPromo() { return prixPromo; }
    public void setPrixPromo(BigDecimal prixPromo) { this.prixPromo = prixPromo; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public Integer getNombreVentes() { return nombreVentes; }
    public void setNombreVentes(Integer nombreVentes) { this.nombreVentes = nombreVentes; }
    public Double getNoteMoyenne() { return noteMoyenne; }
    public void setNoteMoyenne(Double noteMoyenne) { this.noteMoyenne = noteMoyenne; }
    public Integer getNombreAvis() { return nombreAvis; }
    public void setNombreAvis(Integer nombreAvis) { this.nombreAvis = nombreAvis; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }
    public List<ProductVariant> getVariants() { return variants; }
    public void setVariants(List<ProductVariant> variants) { this.variants = variants; }
    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((Product) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() { return "Product{id=" + id + ", nom='" + nom + "'}"; }
}

package com.shopflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank
    @Column(nullable = false)
    private String attribut;

    @NotBlank
    @Column(nullable = false)
    private String valeur;

    private Integer stockSupplementaire = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal prixDelta = BigDecimal.ZERO;

    public ProductVariant() { this.stockSupplementaire = 0; this.prixDelta = BigDecimal.ZERO; }

    public ProductVariant(Long id, Product product, String attribut, String valeur,
                          Integer stockSupplementaire, BigDecimal prixDelta) {
        this.id = id; this.product = product; this.attribut = attribut; this.valeur = valeur;
        this.stockSupplementaire = stockSupplementaire; this.prixDelta = prixDelta;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private Product product; private String attribut; private String valeur;
        private Integer stockSupplementaire = 0; private BigDecimal prixDelta = BigDecimal.ZERO;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder attribut(String attribut) { this.attribut = attribut; return this; }
        public Builder valeur(String valeur) { this.valeur = valeur; return this; }
        public Builder stockSupplementaire(Integer s) { this.stockSupplementaire = s; return this; }
        public Builder prixDelta(BigDecimal prixDelta) { this.prixDelta = prixDelta; return this; }

        public ProductVariant build() { return new ProductVariant(id, product, attribut, valeur, stockSupplementaire, prixDelta); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public String getAttribut() { return attribut; }
    public void setAttribut(String attribut) { this.attribut = attribut; }
    public String getValeur() { return valeur; }
    public void setValeur(String valeur) { this.valeur = valeur; }
    public Integer getStockSupplementaire() { return stockSupplementaire; }
    public void setStockSupplementaire(Integer stockSupplementaire) { this.stockSupplementaire = stockSupplementaire; }
    public BigDecimal getPrixDelta() { return prixDelta; }
    public void setPrixDelta(BigDecimal prixDelta) { this.prixDelta = prixDelta; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((ProductVariant) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

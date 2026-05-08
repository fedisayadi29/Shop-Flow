package com.shopflow.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaire;

    private String nomProduit;

    public OrderItem() {}

    public OrderItem(Long id, Order order, Product product, ProductVariant variant,
                     Integer quantite, BigDecimal prixUnitaire, String nomProduit) {
        this.id = id; this.order = order; this.product = product; this.variant = variant;
        this.quantite = quantite; this.prixUnitaire = prixUnitaire; this.nomProduit = nomProduit;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private Order order; private Product product;
        private ProductVariant variant; private Integer quantite;
        private BigDecimal prixUnitaire; private String nomProduit;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder order(Order order) { this.order = order; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder variant(ProductVariant variant) { this.variant = variant; return this; }
        public Builder quantite(Integer quantite) { this.quantite = quantite; return this; }
        public Builder prixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; return this; }
        public Builder nomProduit(String nomProduit) { this.nomProduit = nomProduit; return this; }

        public OrderItem build() { return new OrderItem(id, order, product, variant, quantite, prixUnitaire, nomProduit); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public ProductVariant getVariant() { return variant; }
    public void setVariant(ProductVariant variant) { this.variant = variant; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public String getNomProduit() { return nomProduit; }
    public void setNomProduit(String nomProduit) { this.nomProduit = nomProduit; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((OrderItem) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

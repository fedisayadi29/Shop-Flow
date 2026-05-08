package com.shopflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.util.Objects;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @Min(1)
    @Column(nullable = false)
    private Integer quantite;

    public CartItem() {}

    public CartItem(Long id, Cart cart, Product product, ProductVariant variant, Integer quantite) {
        this.id = id; this.cart = cart; this.product = product;
        this.variant = variant; this.quantite = quantite;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private Cart cart; private Product product;
        private ProductVariant variant; private Integer quantite;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder cart(Cart cart) { this.cart = cart; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder variant(ProductVariant variant) { this.variant = variant; return this; }
        public Builder quantite(Integer quantite) { this.quantite = quantite; return this; }

        public CartItem build() { return new CartItem(id, cart, product, variant, quantite); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public ProductVariant getVariant() { return variant; }
    public void setVariant(ProductVariant variant) { this.variant = variant; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((CartItem) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

package com.shopflow.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private User customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItem> lignes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private LocalDateTime dateModification = LocalDateTime.now();

    public Cart() {
        this.lignes = new ArrayList<>();
        this.dateModification = LocalDateTime.now();
    }

    public Cart(Long id, User customer, List<CartItem> lignes, Coupon coupon, LocalDateTime dateModification) {
        this.id = id; this.customer = customer; this.lignes = lignes;
        this.coupon = coupon; this.dateModification = dateModification;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private User customer;
        private List<CartItem> lignes = new ArrayList<>();
        private Coupon coupon;
        private LocalDateTime dateModification = LocalDateTime.now();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder customer(User customer) { this.customer = customer; return this; }
        public Builder lignes(List<CartItem> lignes) { this.lignes = lignes; return this; }
        public Builder coupon(Coupon coupon) { this.coupon = coupon; return this; }
        public Builder dateModification(LocalDateTime d) { this.dateModification = d; return this; }

        public Cart build() { return new Cart(id, customer, lignes, coupon, dateModification); }
    }

    @PreUpdate
    public void preUpdate() { this.dateModification = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }
    public List<CartItem> getLignes() { return lignes; }
    public void setLignes(List<CartItem> lignes) { this.lignes = lignes; }
    public Coupon getCoupon() { return coupon; }
    public void setCoupon(Coupon coupon) { this.coupon = coupon; }
    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((Cart) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

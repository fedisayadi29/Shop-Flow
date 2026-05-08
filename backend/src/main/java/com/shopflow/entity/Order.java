package com.shopflow.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus statut = OrderStatus.PENDING;

    @Column(unique = true, nullable = false)
    private String numeroCommande;

    private String adresseLivraisonRue;
    private String adresseLivraisonVille;
    private String adresseLivraisonCodePostal;
    private String adresseLivraisonPays;

    @Column(precision = 10, scale = 2)
    private BigDecimal sousTotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal fraisLivraison = BigDecimal.valueOf(5.99);

    @Column(precision = 10, scale = 2)
    private BigDecimal remiseCoupon;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalTTC;

    @Column(updatable = false)
    private LocalDateTime dateCommande = LocalDateTime.now();

    private boolean isNew = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> lignes = new ArrayList<>();

    public Order() {
        this.statut = OrderStatus.PENDING;
        this.fraisLivraison = BigDecimal.valueOf(5.99);
        this.dateCommande = LocalDateTime.now();
        this.isNew = true;
        this.lignes = new ArrayList<>();
    }

    public Order(Long id, User customer, OrderStatus statut, String numeroCommande,
                 String adresseLivraisonRue, String adresseLivraisonVille,
                 String adresseLivraisonCodePostal, String adresseLivraisonPays,
                 BigDecimal sousTotal, BigDecimal fraisLivraison, BigDecimal remiseCoupon,
                 BigDecimal totalTTC, LocalDateTime dateCommande, boolean isNew,
                 Coupon coupon, List<OrderItem> lignes) {
        this.id = id; this.customer = customer; this.statut = statut;
        this.numeroCommande = numeroCommande;
        this.adresseLivraisonRue = adresseLivraisonRue;
        this.adresseLivraisonVille = adresseLivraisonVille;
        this.adresseLivraisonCodePostal = adresseLivraisonCodePostal;
        this.adresseLivraisonPays = adresseLivraisonPays;
        this.sousTotal = sousTotal; this.fraisLivraison = fraisLivraison;
        this.remiseCoupon = remiseCoupon; this.totalTTC = totalTTC;
        this.dateCommande = dateCommande; this.isNew = isNew;
        this.coupon = coupon; this.lignes = lignes;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private User customer;
        private OrderStatus statut = OrderStatus.PENDING;
        private String numeroCommande;
        private String adresseLivraisonRue; private String adresseLivraisonVille;
        private String adresseLivraisonCodePostal; private String adresseLivraisonPays;
        private BigDecimal sousTotal;
        private BigDecimal fraisLivraison = BigDecimal.valueOf(5.99);
        private BigDecimal remiseCoupon; private BigDecimal totalTTC;
        private LocalDateTime dateCommande = LocalDateTime.now();
        private boolean isNew = true;
        private Coupon coupon; private List<OrderItem> lignes = new ArrayList<>();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder customer(User customer) { this.customer = customer; return this; }
        public Builder statut(OrderStatus statut) { this.statut = statut; return this; }
        public Builder numeroCommande(String n) { this.numeroCommande = n; return this; }
        public Builder adresseLivraisonRue(String s) { this.adresseLivraisonRue = s; return this; }
        public Builder adresseLivraisonVille(String s) { this.adresseLivraisonVille = s; return this; }
        public Builder adresseLivraisonCodePostal(String s) { this.adresseLivraisonCodePostal = s; return this; }
        public Builder adresseLivraisonPays(String s) { this.adresseLivraisonPays = s; return this; }
        public Builder sousTotal(BigDecimal s) { this.sousTotal = s; return this; }
        public Builder fraisLivraison(BigDecimal f) { this.fraisLivraison = f; return this; }
        public Builder remiseCoupon(BigDecimal r) { this.remiseCoupon = r; return this; }
        public Builder totalTTC(BigDecimal t) { this.totalTTC = t; return this; }
        public Builder dateCommande(LocalDateTime d) { this.dateCommande = d; return this; }
        public Builder isNew(boolean isNew) { this.isNew = isNew; return this; }
        public Builder coupon(Coupon coupon) { this.coupon = coupon; return this; }
        public Builder lignes(List<OrderItem> lignes) { this.lignes = lignes; return this; }

        public Order build() {
            return new Order(id, customer, statut, numeroCommande, adresseLivraisonRue,
                    adresseLivraisonVille, adresseLivraisonCodePostal, adresseLivraisonPays,
                    sousTotal, fraisLivraison, remiseCoupon, totalTTC, dateCommande, isNew, coupon, lignes);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }
    public OrderStatus getStatut() { return statut; }
    public void setStatut(OrderStatus statut) { this.statut = statut; }
    public String getNumeroCommande() { return numeroCommande; }
    public void setNumeroCommande(String numeroCommande) { this.numeroCommande = numeroCommande; }
    public String getAdresseLivraisonRue() { return adresseLivraisonRue; }
    public void setAdresseLivraisonRue(String s) { this.adresseLivraisonRue = s; }
    public String getAdresseLivraisonVille() { return adresseLivraisonVille; }
    public void setAdresseLivraisonVille(String s) { this.adresseLivraisonVille = s; }
    public String getAdresseLivraisonCodePostal() { return adresseLivraisonCodePostal; }
    public void setAdresseLivraisonCodePostal(String s) { this.adresseLivraisonCodePostal = s; }
    public String getAdresseLivraisonPays() { return adresseLivraisonPays; }
    public void setAdresseLivraisonPays(String s) { this.adresseLivraisonPays = s; }
    public BigDecimal getSousTotal() { return sousTotal; }
    public void setSousTotal(BigDecimal sousTotal) { this.sousTotal = sousTotal; }
    public BigDecimal getFraisLivraison() { return fraisLivraison; }
    public void setFraisLivraison(BigDecimal fraisLivraison) { this.fraisLivraison = fraisLivraison; }
    public BigDecimal getRemiseCoupon() { return remiseCoupon; }
    public void setRemiseCoupon(BigDecimal remiseCoupon) { this.remiseCoupon = remiseCoupon; }
    public BigDecimal getTotalTTC() { return totalTTC; }
    public void setTotalTTC(BigDecimal totalTTC) { this.totalTTC = totalTTC; }
    public LocalDateTime getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDateTime dateCommande) { this.dateCommande = dateCommande; }
    public boolean isNew() { return isNew; }
    public void setNew(boolean isNew) { this.isNew = isNew; }
    public Coupon getCoupon() { return coupon; }
    public void setCoupon(Coupon coupon) { this.coupon = coupon; }
    public List<OrderItem> getLignes() { return lignes; }
    public void setLignes(List<OrderItem> lignes) { this.lignes = lignes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((Order) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() { return "Order{id=" + id + ", numeroCommande='" + numeroCommande + "', statut=" + statut + "}"; }
}

package com.shopflow.dto.response;

import com.shopflow.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id; private String numeroCommande; private OrderStatus statut;
    private String adresseLivraisonRue; private String adresseLivraisonVille;
    private String adresseLivraisonCodePostal; private String adresseLivraisonPays;
    private BigDecimal sousTotal; private BigDecimal fraisLivraison;
    private BigDecimal remiseCoupon; private BigDecimal totalTTC;
    private LocalDateTime dateCommande; private boolean isNew;
    private List<OrderItemResponse> lignes; private CouponResponse coupon;

    public OrderResponse() {}

    public OrderResponse(Long id, String numeroCommande, OrderStatus statut,
                         String adresseLivraisonRue, String adresseLivraisonVille,
                         String adresseLivraisonCodePostal, String adresseLivraisonPays,
                         BigDecimal sousTotal, BigDecimal fraisLivraison, BigDecimal remiseCoupon,
                         BigDecimal totalTTC, LocalDateTime dateCommande, boolean isNew,
                         List<OrderItemResponse> lignes, CouponResponse coupon) {
        this.id = id; this.numeroCommande = numeroCommande; this.statut = statut;
        this.adresseLivraisonRue = adresseLivraisonRue; this.adresseLivraisonVille = adresseLivraisonVille;
        this.adresseLivraisonCodePostal = adresseLivraisonCodePostal; this.adresseLivraisonPays = adresseLivraisonPays;
        this.sousTotal = sousTotal; this.fraisLivraison = fraisLivraison;
        this.remiseCoupon = remiseCoupon; this.totalTTC = totalTTC;
        this.dateCommande = dateCommande; this.isNew = isNew;
        this.lignes = lignes; this.coupon = coupon;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroCommande() { return numeroCommande; }
    public void setNumeroCommande(String numeroCommande) { this.numeroCommande = numeroCommande; }
    public OrderStatus getStatut() { return statut; }
    public void setStatut(OrderStatus statut) { this.statut = statut; }
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
    public List<OrderItemResponse> getLignes() { return lignes; }
    public void setLignes(List<OrderItemResponse> lignes) { this.lignes = lignes; }
    public CouponResponse getCoupon() { return coupon; }
    public void setCoupon(CouponResponse coupon) { this.coupon = coupon; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String numeroCommande; private OrderStatus statut;
        private String adresseLivraisonRue; private String adresseLivraisonVille;
        private String adresseLivraisonCodePostal; private String adresseLivraisonPays;
        private BigDecimal sousTotal; private BigDecimal fraisLivraison;
        private BigDecimal remiseCoupon; private BigDecimal totalTTC;
        private LocalDateTime dateCommande; private boolean isNew;
        private List<OrderItemResponse> lignes; private CouponResponse coupon;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder numeroCommande(String n) { this.numeroCommande = n; return this; }
        public Builder statut(OrderStatus statut) { this.statut = statut; return this; }
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
        public Builder lignes(List<OrderItemResponse> lignes) { this.lignes = lignes; return this; }
        public Builder coupon(CouponResponse coupon) { this.coupon = coupon; return this; }

        public OrderResponse build() {
            return new OrderResponse(id, numeroCommande, statut, adresseLivraisonRue, adresseLivraisonVille,
                    adresseLivraisonCodePostal, adresseLivraisonPays, sousTotal, fraisLivraison,
                    remiseCoupon, totalTTC, dateCommande, isNew, lignes, coupon);
        }
    }
}

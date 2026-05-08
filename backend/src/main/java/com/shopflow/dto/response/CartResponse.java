package com.shopflow.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartResponse {

    private Long id; private List<CartItemResponse> lignes; private CouponResponse coupon;
    private BigDecimal sousTotal; private BigDecimal remiseCoupon;
    private BigDecimal fraisLivraison; private BigDecimal totalTTC;
    private LocalDateTime dateModification;

    public CartResponse() {}

    public CartResponse(Long id, List<CartItemResponse> lignes, CouponResponse coupon,
                        BigDecimal sousTotal, BigDecimal remiseCoupon, BigDecimal fraisLivraison,
                        BigDecimal totalTTC, LocalDateTime dateModification) {
        this.id = id; this.lignes = lignes; this.coupon = coupon; this.sousTotal = sousTotal;
        this.remiseCoupon = remiseCoupon; this.fraisLivraison = fraisLivraison;
        this.totalTTC = totalTTC; this.dateModification = dateModification;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public List<CartItemResponse> getLignes() { return lignes; }
    public void setLignes(List<CartItemResponse> lignes) { this.lignes = lignes; }
    public CouponResponse getCoupon() { return coupon; }
    public void setCoupon(CouponResponse coupon) { this.coupon = coupon; }
    public BigDecimal getSousTotal() { return sousTotal; }
    public void setSousTotal(BigDecimal sousTotal) { this.sousTotal = sousTotal; }
    public BigDecimal getRemiseCoupon() { return remiseCoupon; }
    public void setRemiseCoupon(BigDecimal remiseCoupon) { this.remiseCoupon = remiseCoupon; }
    public BigDecimal getFraisLivraison() { return fraisLivraison; }
    public void setFraisLivraison(BigDecimal fraisLivraison) { this.fraisLivraison = fraisLivraison; }
    public BigDecimal getTotalTTC() { return totalTTC; }
    public void setTotalTTC(BigDecimal totalTTC) { this.totalTTC = totalTTC; }
    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private List<CartItemResponse> lignes; private CouponResponse coupon;
        private BigDecimal sousTotal; private BigDecimal remiseCoupon;
        private BigDecimal fraisLivraison; private BigDecimal totalTTC;
        private LocalDateTime dateModification;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder lignes(List<CartItemResponse> lignes) { this.lignes = lignes; return this; }
        public Builder coupon(CouponResponse coupon) { this.coupon = coupon; return this; }
        public Builder sousTotal(BigDecimal sousTotal) { this.sousTotal = sousTotal; return this; }
        public Builder remiseCoupon(BigDecimal remiseCoupon) { this.remiseCoupon = remiseCoupon; return this; }
        public Builder fraisLivraison(BigDecimal fraisLivraison) { this.fraisLivraison = fraisLivraison; return this; }
        public Builder totalTTC(BigDecimal totalTTC) { this.totalTTC = totalTTC; return this; }
        public Builder dateModification(LocalDateTime dateModification) { this.dateModification = dateModification; return this; }

        public CartResponse build() {
            return new CartResponse(id, lignes, coupon, sousTotal, remiseCoupon, fraisLivraison, totalTTC, dateModification);
        }
    }
}

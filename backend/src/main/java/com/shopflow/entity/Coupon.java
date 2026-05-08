package com.shopflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valeur;

    private LocalDateTime dateExpiration;
    private Integer usagesMax = 100;
    private Integer usagesActuels = 0;
    private boolean actif = true;

    public Coupon() { this.usagesMax = 100; this.usagesActuels = 0; this.actif = true; }

    public Coupon(Long id, String code, CouponType type, BigDecimal valeur,
                  LocalDateTime dateExpiration, Integer usagesMax, Integer usagesActuels, boolean actif) {
        this.id = id; this.code = code; this.type = type; this.valeur = valeur;
        this.dateExpiration = dateExpiration; this.usagesMax = usagesMax;
        this.usagesActuels = usagesActuels; this.actif = actif;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String code; private CouponType type; private BigDecimal valeur;
        private LocalDateTime dateExpiration; private Integer usagesMax = 100;
        private Integer usagesActuels = 0; private boolean actif = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder code(String code) { this.code = code; return this; }
        public Builder type(CouponType type) { this.type = type; return this; }
        public Builder valeur(BigDecimal valeur) { this.valeur = valeur; return this; }
        public Builder dateExpiration(LocalDateTime d) { this.dateExpiration = d; return this; }
        public Builder usagesMax(Integer usagesMax) { this.usagesMax = usagesMax; return this; }
        public Builder usagesActuels(Integer usagesActuels) { this.usagesActuels = usagesActuels; return this; }
        public Builder actif(boolean actif) { this.actif = actif; return this; }

        public Coupon build() { return new Coupon(id, code, type, valeur, dateExpiration, usagesMax, usagesActuels, actif); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public CouponType getType() { return type; }
    public void setType(CouponType type) { this.type = type; }
    public BigDecimal getValeur() { return valeur; }
    public void setValeur(BigDecimal valeur) { this.valeur = valeur; }
    public LocalDateTime getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDateTime dateExpiration) { this.dateExpiration = dateExpiration; }
    public Integer getUsagesMax() { return usagesMax; }
    public void setUsagesMax(Integer usagesMax) { this.usagesMax = usagesMax; }
    public Integer getUsagesActuels() { return usagesActuels; }
    public void setUsagesActuels(Integer usagesActuels) { this.usagesActuels = usagesActuels; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((Coupon) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

package com.shopflow.dto.response;

import com.shopflow.entity.CouponType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponResponse {

    private Long id; private String code; private CouponType type; private BigDecimal valeur;
    private LocalDateTime dateExpiration; private Integer usagesMax; private Integer usagesActuels; private boolean actif;

    public CouponResponse() {}

    public CouponResponse(Long id, String code, CouponType type, BigDecimal valeur,
                          LocalDateTime dateExpiration, Integer usagesMax, Integer usagesActuels, boolean actif) {
        this.id = id; this.code = code; this.type = type; this.valeur = valeur;
        this.dateExpiration = dateExpiration; this.usagesMax = usagesMax;
        this.usagesActuels = usagesActuels; this.actif = actif;
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

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String code; private CouponType type; private BigDecimal valeur;
        private LocalDateTime dateExpiration; private Integer usagesMax; private Integer usagesActuels; private boolean actif;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder code(String code) { this.code = code; return this; }
        public Builder type(CouponType type) { this.type = type; return this; }
        public Builder valeur(BigDecimal valeur) { this.valeur = valeur; return this; }
        public Builder dateExpiration(LocalDateTime dateExpiration) { this.dateExpiration = dateExpiration; return this; }
        public Builder usagesMax(Integer usagesMax) { this.usagesMax = usagesMax; return this; }
        public Builder usagesActuels(Integer usagesActuels) { this.usagesActuels = usagesActuels; return this; }
        public Builder actif(boolean actif) { this.actif = actif; return this; }

        public CouponResponse build() {
            return new CouponResponse(id, code, type, valeur, dateExpiration, usagesMax, usagesActuels, actif);
        }
    }
}

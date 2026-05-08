package com.shopflow.dto.request;

import com.shopflow.entity.CouponType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponRequest {

    @NotBlank
    private String code;

    @NotNull
    private CouponType type;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal valeur;

    private LocalDateTime dateExpiration;
    private Integer usagesMax = 100;

    public CouponRequest() {}

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
}

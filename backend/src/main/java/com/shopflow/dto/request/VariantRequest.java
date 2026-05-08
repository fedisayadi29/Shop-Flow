package com.shopflow.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class VariantRequest {

    @NotBlank
    private String attribut;

    @NotBlank
    private String valeur;

    private Integer stockSupplementaire = 0;
    private BigDecimal prixDelta = BigDecimal.ZERO;

    public VariantRequest() {}

    public String getAttribut() { return attribut; }
    public void setAttribut(String attribut) { this.attribut = attribut; }
    public String getValeur() { return valeur; }
    public void setValeur(String valeur) { this.valeur = valeur; }
    public Integer getStockSupplementaire() { return stockSupplementaire; }
    public void setStockSupplementaire(Integer stockSupplementaire) { this.stockSupplementaire = stockSupplementaire; }
    public BigDecimal getPrixDelta() { return prixDelta; }
    public void setPrixDelta(BigDecimal prixDelta) { this.prixDelta = prixDelta; }
}

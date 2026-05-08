package com.shopflow.dto.response;

import java.math.BigDecimal;

public class VariantResponse {

    private Long id; private String attribut; private String valeur;
    private Integer stockSupplementaire; private BigDecimal prixDelta;

    public VariantResponse() {}

    public VariantResponse(Long id, String attribut, String valeur, Integer stockSupplementaire, BigDecimal prixDelta) {
        this.id = id; this.attribut = attribut; this.valeur = valeur;
        this.stockSupplementaire = stockSupplementaire; this.prixDelta = prixDelta;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAttribut() { return attribut; }
    public void setAttribut(String attribut) { this.attribut = attribut; }
    public String getValeur() { return valeur; }
    public void setValeur(String valeur) { this.valeur = valeur; }
    public Integer getStockSupplementaire() { return stockSupplementaire; }
    public void setStockSupplementaire(Integer stockSupplementaire) { this.stockSupplementaire = stockSupplementaire; }
    public BigDecimal getPrixDelta() { return prixDelta; }
    public void setPrixDelta(BigDecimal prixDelta) { this.prixDelta = prixDelta; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String attribut; private String valeur;
        private Integer stockSupplementaire; private BigDecimal prixDelta;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder attribut(String attribut) { this.attribut = attribut; return this; }
        public Builder valeur(String valeur) { this.valeur = valeur; return this; }
        public Builder stockSupplementaire(Integer s) { this.stockSupplementaire = s; return this; }
        public Builder prixDelta(BigDecimal prixDelta) { this.prixDelta = prixDelta; return this; }

        public VariantResponse build() { return new VariantResponse(id, attribut, valeur, stockSupplementaire, prixDelta); }
    }
}

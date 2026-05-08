package com.shopflow.dto.response;

import java.math.BigDecimal;

public class CartItemResponse {

    private Long id; private Long productId; private String nomProduit; private String imageProduit;
    private BigDecimal prixUnitaire; private Long variantId; private String variantInfo;
    private Integer quantite; private BigDecimal sousTotal; private Integer stockDisponible;

    public CartItemResponse() {}

    public CartItemResponse(Long id, Long productId, String nomProduit, String imageProduit,
                            BigDecimal prixUnitaire, Long variantId, String variantInfo,
                            Integer quantite, BigDecimal sousTotal, Integer stockDisponible) {
        this.id = id; this.productId = productId; this.nomProduit = nomProduit;
        this.imageProduit = imageProduit; this.prixUnitaire = prixUnitaire;
        this.variantId = variantId; this.variantInfo = variantInfo;
        this.quantite = quantite; this.sousTotal = sousTotal; this.stockDisponible = stockDisponible;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getNomProduit() { return nomProduit; }
    public void setNomProduit(String nomProduit) { this.nomProduit = nomProduit; }
    public String getImageProduit() { return imageProduit; }
    public void setImageProduit(String imageProduit) { this.imageProduit = imageProduit; }
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public Long getVariantId() { return variantId; }
    public void setVariantId(Long variantId) { this.variantId = variantId; }
    public String getVariantInfo() { return variantInfo; }
    public void setVariantInfo(String variantInfo) { this.variantInfo = variantInfo; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public BigDecimal getSousTotal() { return sousTotal; }
    public void setSousTotal(BigDecimal sousTotal) { this.sousTotal = sousTotal; }
    public Integer getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(Integer stockDisponible) { this.stockDisponible = stockDisponible; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private Long productId; private String nomProduit; private String imageProduit;
        private BigDecimal prixUnitaire; private Long variantId; private String variantInfo;
        private Integer quantite; private BigDecimal sousTotal; private Integer stockDisponible;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder productId(Long productId) { this.productId = productId; return this; }
        public Builder nomProduit(String nomProduit) { this.nomProduit = nomProduit; return this; }
        public Builder imageProduit(String imageProduit) { this.imageProduit = imageProduit; return this; }
        public Builder prixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; return this; }
        public Builder variantId(Long variantId) { this.variantId = variantId; return this; }
        public Builder variantInfo(String variantInfo) { this.variantInfo = variantInfo; return this; }
        public Builder quantite(Integer quantite) { this.quantite = quantite; return this; }
        public Builder sousTotal(BigDecimal sousTotal) { this.sousTotal = sousTotal; return this; }
        public Builder stockDisponible(Integer stockDisponible) { this.stockDisponible = stockDisponible; return this; }

        public CartItemResponse build() {
            return new CartItemResponse(id, productId, nomProduit, imageProduit, prixUnitaire,
                    variantId, variantInfo, quantite, sousTotal, stockDisponible);
        }
    }
}

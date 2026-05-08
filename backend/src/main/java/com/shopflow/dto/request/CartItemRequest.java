package com.shopflow.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "ID produit obligatoire")
    private Long productId;

    private Long variantId;

    @NotNull
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantite;

    public CartItemRequest() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getVariantId() { return variantId; }
    public void setVariantId(Long variantId) { this.variantId = variantId; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
}

package com.shopflow.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class ProductRequest {

    @NotBlank(message = "Nom du produit obligatoire")
    private String nom;

    private String description;

    @NotNull(message = "Prix obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix doit être positif")
    private BigDecimal prix;

    private BigDecimal prixPromo;
    private Integer stock = 0;
    private List<String> images;
    private List<Long> categoryIds;
    private List<VariantRequest> variants;

    public ProductRequest() {}

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }
    public BigDecimal getPrixPromo() { return prixPromo; }
    public void setPrixPromo(BigDecimal prixPromo) { this.prixPromo = prixPromo; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
    public List<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(List<Long> categoryIds) { this.categoryIds = categoryIds; }
    public List<VariantRequest> getVariants() { return variants; }
    public void setVariants(List<VariantRequest> variants) { this.variants = variants; }
}

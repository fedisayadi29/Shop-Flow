package com.shopflow.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ProductResponse {

    private Long id; private String nom; private String description;
    private BigDecimal prix; private BigDecimal prixPromo; private Integer stock;
    private boolean actif; private LocalDateTime dateCreation;
    private Integer nombreVentes; private Double noteMoyenne; private Integer nombreAvis;
    private boolean enPromotion; private Double pourcentageRemise;
    private List<String> images; private List<CategoryResponse> categories;
    private List<VariantResponse> variants; private SellerSummaryResponse seller;

    public ProductResponse() {}

    public ProductResponse(Long id, String nom, String description, BigDecimal prix, BigDecimal prixPromo,
                           Integer stock, boolean actif, LocalDateTime dateCreation, Integer nombreVentes,
                           Double noteMoyenne, Integer nombreAvis, boolean enPromotion, Double pourcentageRemise,
                           List<String> images, List<CategoryResponse> categories, List<VariantResponse> variants,
                           SellerSummaryResponse seller) {
        this.id = id; this.nom = nom; this.description = description; this.prix = prix;
        this.prixPromo = prixPromo; this.stock = stock; this.actif = actif;
        this.dateCreation = dateCreation; this.nombreVentes = nombreVentes;
        this.noteMoyenne = noteMoyenne; this.nombreAvis = nombreAvis;
        this.enPromotion = enPromotion; this.pourcentageRemise = pourcentageRemise;
        this.images = images; this.categories = categories; this.variants = variants; this.seller = seller;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public Integer getNombreVentes() { return nombreVentes; }
    public void setNombreVentes(Integer nombreVentes) { this.nombreVentes = nombreVentes; }
    public Double getNoteMoyenne() { return noteMoyenne; }
    public void setNoteMoyenne(Double noteMoyenne) { this.noteMoyenne = noteMoyenne; }
    public Integer getNombreAvis() { return nombreAvis; }
    public void setNombreAvis(Integer nombreAvis) { this.nombreAvis = nombreAvis; }
    public boolean isEnPromotion() { return enPromotion; }
    public void setEnPromotion(boolean enPromotion) { this.enPromotion = enPromotion; }
    public Double getPourcentageRemise() { return pourcentageRemise; }
    public void setPourcentageRemise(Double pourcentageRemise) { this.pourcentageRemise = pourcentageRemise; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
    public List<CategoryResponse> getCategories() { return categories; }
    public void setCategories(List<CategoryResponse> categories) { this.categories = categories; }
    public List<VariantResponse> getVariants() { return variants; }
    public void setVariants(List<VariantResponse> variants) { this.variants = variants; }
    public SellerSummaryResponse getSeller() { return seller; }
    public void setSeller(SellerSummaryResponse seller) { this.seller = seller; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String nom; private String description;
        private BigDecimal prix; private BigDecimal prixPromo; private Integer stock;
        private boolean actif; private LocalDateTime dateCreation;
        private Integer nombreVentes; private Double noteMoyenne; private Integer nombreAvis;
        private boolean enPromotion; private Double pourcentageRemise;
        private List<String> images; private List<CategoryResponse> categories;
        private List<VariantResponse> variants; private SellerSummaryResponse seller;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder nom(String nom) { this.nom = nom; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder prix(BigDecimal prix) { this.prix = prix; return this; }
        public Builder prixPromo(BigDecimal prixPromo) { this.prixPromo = prixPromo; return this; }
        public Builder stock(Integer stock) { this.stock = stock; return this; }
        public Builder actif(boolean actif) { this.actif = actif; return this; }
        public Builder dateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; return this; }
        public Builder nombreVentes(Integer nombreVentes) { this.nombreVentes = nombreVentes; return this; }
        public Builder noteMoyenne(Double noteMoyenne) { this.noteMoyenne = noteMoyenne; return this; }
        public Builder nombreAvis(Integer nombreAvis) { this.nombreAvis = nombreAvis; return this; }
        public Builder enPromotion(boolean enPromotion) { this.enPromotion = enPromotion; return this; }
        public Builder pourcentageRemise(Double pourcentageRemise) { this.pourcentageRemise = pourcentageRemise; return this; }
        public Builder images(List<String> images) { this.images = images; return this; }
        public Builder categories(List<CategoryResponse> categories) { this.categories = categories; return this; }
        public Builder variants(List<VariantResponse> variants) { this.variants = variants; return this; }
        public Builder seller(SellerSummaryResponse seller) { this.seller = seller; return this; }

        public ProductResponse build() {
            return new ProductResponse(id, nom, description, prix, prixPromo, stock, actif, dateCreation,
                    nombreVentes, noteMoyenne, nombreAvis, enPromotion, pourcentageRemise,
                    images, categories, variants, seller);
        }
    }
}

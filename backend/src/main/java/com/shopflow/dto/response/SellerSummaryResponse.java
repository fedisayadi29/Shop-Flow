package com.shopflow.dto.response;

public class SellerSummaryResponse {

    private Long id; private String prenom; private String nom;
    private String nomBoutique; private String logo;

    public SellerSummaryResponse() {}

    public SellerSummaryResponse(Long id, String prenom, String nom, String nomBoutique, String logo) {
        this.id = id; this.prenom = prenom; this.nom = nom;
        this.nomBoutique = nomBoutique; this.logo = logo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getNomBoutique() { return nomBoutique; }
    public void setNomBoutique(String nomBoutique) { this.nomBoutique = nomBoutique; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String prenom; private String nom;
        private String nomBoutique; private String logo;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder prenom(String prenom) { this.prenom = prenom; return this; }
        public Builder nom(String nom) { this.nom = nom; return this; }
        public Builder nomBoutique(String nomBoutique) { this.nomBoutique = nomBoutique; return this; }
        public Builder logo(String logo) { this.logo = logo; return this; }

        public SellerSummaryResponse build() { return new SellerSummaryResponse(id, prenom, nom, nomBoutique, logo); }
    }
}

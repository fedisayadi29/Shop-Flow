package com.shopflow.dto.response;

public class SellerProfileResponse {

    private Long id; private String nomBoutique; private String description;
    private String logo; private Double note;

    public SellerProfileResponse() {}

    public SellerProfileResponse(Long id, String nomBoutique, String description, String logo, Double note) {
        this.id = id; this.nomBoutique = nomBoutique; this.description = description;
        this.logo = logo; this.note = note;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomBoutique() { return nomBoutique; }
    public void setNomBoutique(String nomBoutique) { this.nomBoutique = nomBoutique; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public Double getNote() { return note; }
    public void setNote(Double note) { this.note = note; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String nomBoutique; private String description;
        private String logo; private Double note;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder nomBoutique(String nomBoutique) { this.nomBoutique = nomBoutique; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder logo(String logo) { this.logo = logo; return this; }
        public Builder note(Double note) { this.note = note; return this; }

        public SellerProfileResponse build() { return new SellerProfileResponse(id, nomBoutique, description, logo, note); }
    }
}

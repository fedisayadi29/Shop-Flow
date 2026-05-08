package com.shopflow.dto.response;

import java.time.LocalDateTime;

public class ReviewResponse {

    private Long id; private Long productId; private String customerPrenom; private String customerNom;
    private Integer note; private String commentaire; private LocalDateTime dateCreation; private boolean approuve;

    public ReviewResponse() {}

    public ReviewResponse(Long id, Long productId, String customerPrenom, String customerNom,
                          Integer note, String commentaire, LocalDateTime dateCreation, boolean approuve) {
        this.id = id; this.productId = productId; this.customerPrenom = customerPrenom;
        this.customerNom = customerNom; this.note = note; this.commentaire = commentaire;
        this.dateCreation = dateCreation; this.approuve = approuve;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getCustomerPrenom() { return customerPrenom; }
    public void setCustomerPrenom(String customerPrenom) { this.customerPrenom = customerPrenom; }
    public String getCustomerNom() { return customerNom; }
    public void setCustomerNom(String customerNom) { this.customerNom = customerNom; }
    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public boolean isApprouve() { return approuve; }
    public void setApprouve(boolean approuve) { this.approuve = approuve; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private Long productId; private String customerPrenom; private String customerNom;
        private Integer note; private String commentaire; private LocalDateTime dateCreation; private boolean approuve;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder productId(Long productId) { this.productId = productId; return this; }
        public Builder customerPrenom(String customerPrenom) { this.customerPrenom = customerPrenom; return this; }
        public Builder customerNom(String customerNom) { this.customerNom = customerNom; return this; }
        public Builder note(Integer note) { this.note = note; return this; }
        public Builder commentaire(String commentaire) { this.commentaire = commentaire; return this; }
        public Builder dateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; return this; }
        public Builder approuve(boolean approuve) { this.approuve = approuve; return this; }

        public ReviewResponse build() {
            return new ReviewResponse(id, productId, customerPrenom, customerNom, note, commentaire, dateCreation, approuve);
        }
    }
}

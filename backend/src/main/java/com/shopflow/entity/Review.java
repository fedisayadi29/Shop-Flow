package com.shopflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private Integer note;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Column(updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    private boolean approuve = false;

    public Review() { this.dateCreation = LocalDateTime.now(); this.approuve = false; }

    public Review(Long id, User customer, Product product, Integer note, String commentaire,
                  LocalDateTime dateCreation, boolean approuve) {
        this.id = id; this.customer = customer; this.product = product; this.note = note;
        this.commentaire = commentaire; this.dateCreation = dateCreation; this.approuve = approuve;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private User customer; private Product product; private Integer note;
        private String commentaire; private LocalDateTime dateCreation = LocalDateTime.now();
        private boolean approuve = false;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder customer(User customer) { this.customer = customer; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder note(Integer note) { this.note = note; return this; }
        public Builder commentaire(String commentaire) { this.commentaire = commentaire; return this; }
        public Builder dateCreation(LocalDateTime d) { this.dateCreation = d; return this; }
        public Builder approuve(boolean approuve) { this.approuve = approuve; return this; }

        public Review build() { return new Review(id, customer, product, note, commentaire, dateCreation, approuve); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public boolean isApprouve() { return approuve; }
    public void setApprouve(boolean approuve) { this.approuve = approuve; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((Review) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

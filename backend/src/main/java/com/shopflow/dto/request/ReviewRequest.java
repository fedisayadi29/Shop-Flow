package com.shopflow.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReviewRequest {

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer note;

    private String commentaire;

    public ReviewRequest() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}

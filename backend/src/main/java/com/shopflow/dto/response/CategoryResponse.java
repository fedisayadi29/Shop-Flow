package com.shopflow.dto.response;

import java.util.List;

public class CategoryResponse {

    private Long id; private String nom; private String description;
    private Long parentId; private List<CategoryResponse> sousCategories;

    public CategoryResponse() {}

    public CategoryResponse(Long id, String nom, String description, Long parentId, List<CategoryResponse> sousCategories) {
        this.id = id; this.nom = nom; this.description = description;
        this.parentId = parentId; this.sousCategories = sousCategories;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public List<CategoryResponse> getSousCategories() { return sousCategories; }
    public void setSousCategories(List<CategoryResponse> sousCategories) { this.sousCategories = sousCategories; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String nom; private String description;
        private Long parentId; private List<CategoryResponse> sousCategories;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder nom(String nom) { this.nom = nom; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder parentId(Long parentId) { this.parentId = parentId; return this; }
        public Builder sousCategories(List<CategoryResponse> sousCategories) { this.sousCategories = sousCategories; return this; }

        public CategoryResponse build() { return new CategoryResponse(id, nom, description, parentId, sousCategories); }
    }
}

package com.shopflow.service;

import com.shopflow.dto.request.CategoryRequest;
import com.shopflow.dto.response.CategoryResponse;
import com.shopflow.entity.Category;
import com.shopflow.exception.ShopFlowException;
import com.shopflow.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoryTree() {
        List<Category> roots = categoryRepository.findRootCategories();
        return roots.stream().map(this::toCategoryResponseWithChildren).collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByNom(request.getNom())) {
            throw ShopFlowException.conflict("Une catégorie avec ce nom existe déjà");
        }

        Category category = Category.builder()
                .nom(request.getNom())
                .description(request.getDescription())
                .build();

        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> ShopFlowException.notFound("Catégorie parente non trouvée"));
            category.setParent(parent);
        }

        category = categoryRepository.save(category);
        return toCategoryResponseWithChildren(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> ShopFlowException.notFound("Catégorie non trouvée : " + id));

        category.setNom(request.getNom());
        category.setDescription(request.getDescription());

        if (request.getParentId() != null) {
            if (request.getParentId().equals(id)) {
                throw ShopFlowException.badRequest("Une catégorie ne peut pas être son propre parent");
            }
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> ShopFlowException.notFound("Catégorie parente non trouvée"));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        category = categoryRepository.save(category);
        return toCategoryResponseWithChildren(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> ShopFlowException.notFound("Catégorie non trouvée : " + id));
        categoryRepository.delete(category);
    }

    public CategoryResponse toCategoryResponseWithChildren(Category category) {
        List<CategoryResponse> children = category.getSousCategories() != null
                ? category.getSousCategories().stream()
                    .map(this::toCategoryResponseWithChildren)
                    .collect(Collectors.toList())
                : List.of();

        return CategoryResponse.builder()
                .id(category.getId())
                .nom(category.getNom())
                .description(category.getDescription())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .sousCategories(children)
                .build();
    }
}

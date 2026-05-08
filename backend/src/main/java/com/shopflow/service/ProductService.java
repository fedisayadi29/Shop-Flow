package com.shopflow.service;

import com.shopflow.dto.request.ProductRequest;
import com.shopflow.dto.request.VariantRequest;
import com.shopflow.dto.response.*;
import com.shopflow.entity.*;
import com.shopflow.exception.ShopFlowException;
import com.shopflow.repository.CategoryRepository;
import com.shopflow.repository.ProductRepository;
import com.shopflow.repository.ProductVariantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository variantRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository,
                          ProductVariantRepository variantRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.variantRepository = variantRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getProducts(
            Long categoryId, BigDecimal prixMin, BigDecimal prixMax,
            Long sellerId, Boolean promo, String sortBy, int page, int size) {

        Sort sort = buildSort(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Product> spec = buildSpecification(categoryId, prixMin, prixMax, sellerId, promo);
        Page<Product> products = productRepository.findAll(spec, pageable);

        return toPageResponse(products.map(this::toProductResponse));
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findByIdWithDetails(id);
        if (product == null) {
            throw ShopFlowException.notFound("Produit non trouvé : " + id);
        }
        return toProductResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request, User seller) {
        Product product = Product.builder()
                .seller(seller)
                .nom(request.getNom())
                .description(request.getDescription())
                .prix(request.getPrix())
                .prixPromo(request.getPrixPromo())
                .stock(request.getStock() != null ? request.getStock() : 0)
                .images(request.getImages() != null ? request.getImages() : new ArrayList<>())
                .actif(true) // Explicitly set actif to true
                .build();

        // Associer les catégories
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            product.setCategories(categories);
        }

        product = productRepository.save(product);
        productRepository.flush(); // Force flush to database

        // Créer les variantes
        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            Product finalProduct = product;
            List<ProductVariant> variants = request.getVariants().stream()
                    .map(v -> buildVariant(v, finalProduct))
                    .collect(Collectors.toList());
            variantRepository.saveAll(variants);
            product.setVariants(variants);
        }

        // Reload product to ensure all lazy collections are initialized
        product = productRepository.findByIdWithDetails(product.getId());
        if (product == null) {
            throw ShopFlowException.notFound("Produit non trouvé après création");
        }
        
        log.info("Product created successfully: id={}, nom={}, actif={}", product.getId(), product.getNom(), product.isActif());
        
        return toProductResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request, User currentUser) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> ShopFlowException.notFound("Produit non trouvé : " + id));

        // Vérifier que le vendeur est propriétaire ou admin
        if (currentUser.getRole() != Role.ADMIN && !product.getSeller().getId().equals(currentUser.getId())) {
            throw ShopFlowException.forbidden("Vous n'êtes pas autorisé à modifier ce produit");
        }

        product.setNom(request.getNom());
        product.setDescription(request.getDescription());
        product.setPrix(request.getPrix());
        product.setPrixPromo(request.getPrixPromo());
        if (request.getStock() != null) product.setStock(request.getStock());
        if (request.getImages() != null) product.setImages(request.getImages());

        if (request.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            product.setCategories(categories);
        }

        product = productRepository.save(product);
        return toProductResponse(product);
    }

    @Transactional
    public void deleteProduct(Long id, User currentUser) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> ShopFlowException.notFound("Produit non trouvé : " + id));

        if (currentUser.getRole() != Role.ADMIN && !product.getSeller().getId().equals(currentUser.getId())) {
            throw ShopFlowException.forbidden("Vous n'êtes pas autorisé à supprimer ce produit");
        }

        // Soft delete
        product.setActif(false);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> searchProducts(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.searchFullText(q, pageable);
        return toPageResponse(products.map(this::toProductResponse));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getTopSelling() {
        Pageable pageable = PageRequest.of(0, 10);
        return productRepository.findTopSelling(pageable)
                .stream().map(this::toProductResponse).collect(Collectors.toList());
    }

    // ---- Mappers ----

    public ProductResponse toProductResponse(Product product) {
        // Force initialization of lazy collections
        List<CategoryResponse> categories = new ArrayList<>();
        if (product.getCategories() != null) {
            product.getCategories().size(); // Force initialization
            categories = product.getCategories().stream()
                    .map(this::toCategoryResponse)
                    .collect(Collectors.toList());
        }

        List<VariantResponse> variants = new ArrayList<>();
        if (product.getVariants() != null) {
            product.getVariants().size(); // Force initialization
            variants = product.getVariants().stream()
                    .map(this::toVariantResponse)
                    .collect(Collectors.toList());
        }

        List<String> images = new ArrayList<>();
        if (product.getImages() != null) {
            product.getImages().size(); // Force initialization
            images = new ArrayList<>(product.getImages());
        }

        SellerSummaryResponse sellerSummary = null;
        if (product.getSeller() != null) {
            User seller = product.getSeller();
            String nomBoutique = null;
            String logo = null;
            if (seller.getSellerProfile() != null) {
                nomBoutique = seller.getSellerProfile().getNomBoutique();
                logo = seller.getSellerProfile().getLogo();
            }
            sellerSummary = SellerSummaryResponse.builder()
                    .id(seller.getId())
                    .prenom(seller.getPrenom())
                    .nom(seller.getNom())
                    .nomBoutique(nomBoutique)
                    .logo(logo)
                    .build();
        }

        return ProductResponse.builder()
                .id(product.getId())
                .nom(product.getNom())
                .description(product.getDescription())
                .prix(product.getPrix())
                .prixPromo(product.getPrixPromo())
                .stock(product.getStock())
                .actif(product.isActif())
                .dateCreation(product.getDateCreation())
                .nombreVentes(product.getNombreVentes())
                .noteMoyenne(product.getNoteMoyenne())
                .nombreAvis(product.getNombreAvis())
                .enPromotion(product.isEnPromotion())
                .pourcentageRemise(product.getPourcentageRemise())
                .images(images)
                .categories(categories)
                .variants(variants)
                .seller(sellerSummary)
                .build();
    }

    private CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .nom(category.getNom())
                .description(category.getDescription())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .build();
    }

    private VariantResponse toVariantResponse(ProductVariant variant) {
        return VariantResponse.builder()
                .id(variant.getId())
                .attribut(variant.getAttribut())
                .valeur(variant.getValeur())
                .stockSupplementaire(variant.getStockSupplementaire())
                .prixDelta(variant.getPrixDelta())
                .build();
    }

    private ProductVariant buildVariant(VariantRequest req, Product product) {
        return ProductVariant.builder()
                .product(product)
                .attribut(req.getAttribut())
                .valeur(req.getValeur())
                .stockSupplementaire(req.getStockSupplementaire())
                .prixDelta(req.getPrixDelta())
                .build();
    }

    private Sort buildSort(String sortBy) {
        if (sortBy == null) return Sort.by(Sort.Direction.DESC, "dateCreation");
        return switch (sortBy) {
            case "prix_asc" -> Sort.by(Sort.Direction.ASC, "prix");
            case "prix_desc" -> Sort.by(Sort.Direction.DESC, "prix");
            case "popularite" -> Sort.by(Sort.Direction.DESC, "nombreVentes");
            case "note" -> Sort.by(Sort.Direction.DESC, "noteMoyenne");
            default -> Sort.by(Sort.Direction.DESC, "dateCreation");
        };
    }

    private Specification<Product> buildSpecification(
            Long categoryId, BigDecimal prixMin, BigDecimal prixMax, Long sellerId, Boolean promo) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("actif")));

            if (categoryId != null) {
                var categories = root.join("categories");
                predicates.add(cb.equal(categories.get("id"), categoryId));
            }
            if (prixMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("prix"), prixMin));
            }
            if (prixMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("prix"), prixMax));
            }
            if (sellerId != null) {
                predicates.add(cb.equal(root.get("seller").get("id"), sellerId));
            }
            if (Boolean.TRUE.equals(promo)) {
                predicates.add(cb.isNotNull(root.get("prixPromo")));
                predicates.add(cb.greaterThan(root.get("prixPromo"), BigDecimal.ZERO));
                predicates.add(cb.lessThan(root.get("prixPromo"), root.get("prix")));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    private <T> PageResponse<T> toPageResponse(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .build();
    }
}

package com.shopflow.service;

import com.shopflow.dto.request.ReviewRequest;
import com.shopflow.dto.response.PageResponse;
import com.shopflow.dto.response.ReviewResponse;
import com.shopflow.entity.Product;
import com.shopflow.entity.Review;
import com.shopflow.entity.User;
import com.shopflow.exception.ShopFlowException;
import com.shopflow.repository.ProductRepository;
import com.shopflow.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public ReviewResponse createReview(User customer, ReviewRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> ShopFlowException.notFound("Produit non trouvé"));

        // Vérifier que le client a acheté le produit
        if (!reviewRepository.hasCustomerPurchasedProduct(customer, request.getProductId())) {
            throw ShopFlowException.badRequest("Vous devez avoir acheté ce produit pour laisser un avis");
        }

        // Vérifier qu'il n'a pas déjà laissé un avis
        if (reviewRepository.findByCustomerAndProductId(customer, request.getProductId()).isPresent()) {
            throw ShopFlowException.conflict("Vous avez déjà laissé un avis pour ce produit");
        }

        Review review = Review.builder()
                .customer(customer)
                .product(product)
                .note(request.getNote())
                .commentaire(request.getCommentaire())
                .approuve(false)
                .build();

        review = reviewRepository.save(review);
        return toReviewResponse(review);
    }

    @Transactional(readOnly = true)
    public PageResponse<ReviewResponse> getProductReviews(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreation"));
        Page<Review> reviews = reviewRepository.findByProductIdAndApprouveTrue(productId, pageable);
        return toPageResponse(reviews.map(this::toReviewResponse));
    }

    @Transactional
    public ReviewResponse approveReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> ShopFlowException.notFound("Avis non trouvé : " + id));

        review.setApprouve(true);
        review = reviewRepository.save(review);

        // Recalculer la note moyenne du produit
        updateProductRating(review.getProduct());

        return toReviewResponse(review);
    }

    private void updateProductRating(Product product) {
        Double noteMoyenne = reviewRepository.calculateNoteMoyenne(product.getId());
        Long nombreAvis = reviewRepository.countByProductId(product.getId());
        product.setNoteMoyenne(noteMoyenne != null ? noteMoyenne : 0.0);
        product.setNombreAvis(nombreAvis != null ? nombreAvis.intValue() : 0);
        productRepository.save(product);
    }

    private ReviewResponse toReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .customerPrenom(review.getCustomer().getPrenom())
                .customerNom(review.getCustomer().getNom())
                .note(review.getNote())
                .commentaire(review.getCommentaire())
                .dateCreation(review.getDateCreation())
                .approuve(review.isApprouve())
                .build();
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

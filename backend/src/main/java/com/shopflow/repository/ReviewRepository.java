package com.shopflow.repository;

import com.shopflow.entity.Review;
import com.shopflow.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByProductIdAndApprouveTrue(Long productId, Pageable pageable);

    Page<Review> findByApprouveFalse(Pageable pageable);

    Optional<Review> findByCustomerAndProductId(User customer, Long productId);

    @Query("SELECT AVG(r.note) FROM Review r WHERE r.product.id = :productId AND r.approuve = true")
    Double calculateNoteMoyenne(@Param("productId") Long productId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId AND r.approuve = true")
    Long countByProductId(@Param("productId") Long productId);

    // Vérifie si le client a acheté le produit
    @Query("SELECT COUNT(oi) > 0 FROM OrderItem oi WHERE oi.order.customer = :customer AND oi.product.id = :productId AND oi.order.statut = 'DELIVERED'")
    boolean hasCustomerPurchasedProduct(@Param("customer") User customer, @Param("productId") Long productId);

    List<Review> findByCustomerOrderByDateCreationDesc(User customer);
}

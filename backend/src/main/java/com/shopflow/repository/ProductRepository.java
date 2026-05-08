package com.shopflow.repository;

import com.shopflow.entity.Product;
import com.shopflow.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Page<Product> findByActifTrue(Pageable pageable);
    
    Long countByActifTrue();

    Page<Product> findBySeller(User seller, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.categories " +
           "LEFT JOIN FETCH p.seller " +
           "WHERE p.id = :id")
    Product findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT p FROM Product p WHERE p.actif = true AND " +
           "(LOWER(p.nom) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Product> searchFullText(@Param("q") String q, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.actif = true AND p.prixPromo IS NOT NULL AND p.prixPromo > 0 AND p.prixPromo < p.prix")
    Page<Product> findEnPromotion(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.actif = true ORDER BY p.nombreVentes DESC")
    List<Product> findTopSelling(Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId AND p.actif = true")
    Page<Product> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.actif = true AND p.prix BETWEEN :prixMin AND :prixMax")
    Page<Product> findByPrixBetween(@Param("prixMin") BigDecimal prixMin, @Param("prixMax") BigDecimal prixMax, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.seller = :seller AND p.stock <= :seuil AND p.actif = true")
    Long countLowStockBySeller(@Param("seller") User seller, @Param("seuil") Integer seuil);

    @Query("SELECT p FROM Product p WHERE p.seller = :seller AND p.stock <= :seuil AND p.actif = true")
    List<Product> findLowStockBySeller(@Param("seller") User seller, @Param("seuil") Integer seuil);
}

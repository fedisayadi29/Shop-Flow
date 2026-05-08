package com.shopflow.repository;

import com.shopflow.entity.Order;
import com.shopflow.entity.OrderStatus;
import com.shopflow.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Page<Order> findByCustomer(User customer, Pageable pageable);

    Optional<Order> findByNumeroCommande(String numeroCommande);

    Page<Order> findByStatut(OrderStatus statut, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN o.lignes li WHERE li.product.seller = :seller")
    Page<Order> findBySellerProducts(@Param("seller") User seller, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN o.lignes li WHERE li.product.seller = :seller AND o.statut = :statut")
    Page<Order> findBySellerProductsAndStatut(@Param("seller") User seller, @Param("statut") OrderStatus statut, Pageable pageable);

    @Query("SELECT COALESCE(SUM(o.totalTTC), 0) FROM Order o WHERE o.statut NOT IN ('CANCELLED', 'REFUNDED')")
    BigDecimal calculateChiffreAffairesGlobal();

    @Query("SELECT COALESCE(SUM(li.prixUnitaire * li.quantite), 0) FROM OrderItem li " +
           "WHERE li.product.seller = :seller AND li.order.statut NOT IN ('CANCELLED', 'REFUNDED')")
    BigDecimal calculateRevenusBySeller(@Param("seller") User seller);

    @Query("SELECT COUNT(o) FROM Order o JOIN o.lignes li WHERE li.product.seller = :seller AND o.statut = 'PENDING'")
    Long countCommandesEnAttenteBySeller(@Param("seller") User seller);

    List<Order> findTop10ByOrderByDateCommandeDesc();

    @Query("SELECT o FROM Order o WHERE o.dateCommande BETWEEN :debut AND :fin")
    List<Order> findByDateCommandeBetween(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);
}

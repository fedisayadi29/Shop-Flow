package com.shopflow.repository;

import com.shopflow.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNom(String nom);
    boolean existsByNom(String nom);

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    List<Category> findRootCategories();
}

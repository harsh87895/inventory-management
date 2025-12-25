package com.inventory.repository;

import com.inventory.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.skus WHERE p.id = :id")
    Optional<Product> findByIdWithSkus(@Param("id") Long id);

    @Query("SELECT p FROM Product p " +
           "WHERE (:categoryId IS NULL OR p.category.id = :categoryId) " +
           "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Product> findByCategoryIdAndNameContaining(
        @Param("categoryId") Long categoryId,
        @Param("name") String name,
        Pageable pageable
    );

    boolean existsByNameAndCategoryId(String name, Long categoryId);
} 
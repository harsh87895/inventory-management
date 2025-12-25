package com.inventory.repository;

import com.inventory.model.SKU;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SKURepository extends JpaRepository<SKU, Long> {
    
    @Query("SELECT s FROM SKU s WHERE s.product.id = :productId")
    Page<SKU> findByProductId(@Param("productId") Long productId, Pageable pageable);

    @Query("SELECT s FROM SKU s " +
           "JOIN FETCH s.product p " +
           "JOIN FETCH p.category " +
           "WHERE s.id = :id")
    SKU findByIdWithProductAndCategory(@Param("id") Long id);

    boolean existsByProductIdAndColorAndSize(Long productId, String color, String size);
} 
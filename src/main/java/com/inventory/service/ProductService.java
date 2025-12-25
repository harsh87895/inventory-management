package com.inventory.service;

import com.inventory.dto.request.ProductRequest;
import com.inventory.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    ProductResponse getProductById(Long id);
    Page<ProductResponse> searchProducts(Long categoryId, String name, Pageable pageable);
    void deleteProduct(Long id);
} 
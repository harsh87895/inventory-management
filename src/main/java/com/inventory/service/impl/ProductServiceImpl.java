package com.inventory.service.impl;

import com.inventory.dto.request.ProductRequest;
import com.inventory.dto.response.ProductResponse;
import com.inventory.mapper.ProductMapper;
import com.inventory.model.Category;
import com.inventory.model.Product;
import com.inventory.repository.CategoryRepository;
import com.inventory.repository.ProductRepository;
import com.inventory.service.ProductService;
import com.inventory.service.validation.ProductValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.inventory.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductValidationService validationService;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        // Validate product creation
        validationService.validateProductCreation(request);

        // Get category after validation
        Category category = categoryRepository.findById(request.getCategoryId()).get();

        // Create and save product
        Product product = productMapper.toEntity(request, category);
        Product savedProduct = productRepository.save(product);
        
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        // Validate product update
        validationService.validateProductUpdate(id, request);

        // Get product and category after validation
        Product product = productRepository.findById(id).get();
        Category category = categoryRepository.findById(request.getCategoryId()).get();

        // Update and save product
        productMapper.updateEntity(product, request, category);
        Product updatedProduct = productRepository.save(product);
        
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findByIdWithSkus(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return productMapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> searchProducts(Long categoryId, String name, Pageable pageable) {
        return productRepository.findByCategoryIdAndNameContaining(categoryId, name, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        validationService.validateProductDeletion(id);
        productRepository.deleteById(id);
    }
} 
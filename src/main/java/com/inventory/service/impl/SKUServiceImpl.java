package com.inventory.service.impl;

import com.inventory.dto.request.SKURequest;
import com.inventory.dto.response.SKUResponse;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.model.Product;
import com.inventory.model.SKU;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SKURepository;
import com.inventory.service.SKUService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SKUServiceImpl implements SKUService {

    private final SKURepository skuRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public SKUResponse createSKU(SKURequest request) {
        // Verify product exists
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        // Check if SKU with same color and size exists for the product
        if (skuRepository.existsByProductIdAndColorAndSize(
                request.getProductId(), request.getColor(), request.getSize())) {
            throw new DataIntegrityViolationException(
                String.format("SKU with color '%s' and size '%s' already exists for this product",
                    request.getColor(), request.getSize()));
        }

        SKU sku = new SKU();
        sku.setProduct(product);
        sku.setColor(request.getColor());
        sku.setSize(request.getSize());
        sku.setPrice(request.getPrice());
        sku.setStockQuantity(request.getStockQuantity());

        SKU savedSKU = skuRepository.save(sku);
        return mapToSKUResponse(savedSKU);
    }

    @Override
    @Transactional
    public SKUResponse updateSKU(Long id, SKURequest request) {
        SKU sku = skuRepository.findByIdWithProductAndCategory(id);
        if (sku == null) {
            throw new ResourceNotFoundException("SKU", "id", id);
        }

        // If product is being changed, verify new product exists
        if (!sku.getProduct().getId().equals(request.getProductId())) {
            Product newProduct = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

            // Check if SKU with same color and size exists for the new product
            if (skuRepository.existsByProductIdAndColorAndSize(
                    request.getProductId(), request.getColor(), request.getSize())) {
                throw new DataIntegrityViolationException(
                    String.format("SKU with color '%s' and size '%s' already exists for the target product",
                        request.getColor(), request.getSize()));
            }
            sku.setProduct(newProduct);
        }

        sku.setColor(request.getColor());
        sku.setSize(request.getSize());
        sku.setPrice(request.getPrice());
        sku.setStockQuantity(request.getStockQuantity());

        SKU updatedSKU = skuRepository.save(sku);
        return mapToSKUResponse(updatedSKU);
    }

    @Override
    public SKUResponse getSKUById(Long id) {
        SKU sku = skuRepository.findByIdWithProductAndCategory(id);
        if (sku == null) {
            throw new ResourceNotFoundException("SKU", "id", id);
        }
        return mapToSKUResponse(sku);
    }

    @Override
    public Page<SKUResponse> getSKUsByProductId(Long productId, Pageable pageable) {
        // Verify product exists
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }
        return skuRepository.findByProductId(productId, pageable)
            .map(this::mapToSKUResponse);
    }

    @Override
    @Transactional
    public void deleteSKU(Long id) {
        if (!skuRepository.existsById(id)) {
            throw new ResourceNotFoundException("SKU", "id", id);
        }
        skuRepository.deleteById(id);
    }

    private SKUResponse mapToSKUResponse(SKU sku) {
        return SKUResponse.builder()
            .id(sku.getId())
            .productId(sku.getProduct().getId())
            .productName(sku.getProduct().getName())
            .color(sku.getColor())
            .size(sku.getSize())
            .price(sku.getPrice())
            .stockQuantity(sku.getStockQuantity())
            .categoryName(sku.getProduct().getCategory().getName())
            .build();
    }
} 
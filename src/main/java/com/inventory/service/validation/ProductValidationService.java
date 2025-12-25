package com.inventory.service.validation;

import com.inventory.dto.request.ProductRequest;
import com.inventory.exception.ProductException;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.model.Category;
import com.inventory.model.Product;
import com.inventory.repository.CategoryRepository;
import com.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductValidationService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public void validateProductCreation(ProductRequest request) {
        // Check if category exists and is active
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
        
        if (!category.isActive()) {
            throw ProductException.categoryNotActive(request.getCategoryId());
        }

        // Check for duplicate product name in category
        if (productRepository.existsByNameAndCategoryId(request.getName(), request.getCategoryId())) {
            throw ProductException.duplicateNameInCategory(request.getName(), request.getCategoryId());
        }

        // Validate description requirement
        validateDescription(request);
    }

    public void validateProductUpdate(Long productId, ProductRequest request) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        // Check if category exists and is active
        Category newCategory = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        if (!newCategory.isActive()) {
            throw ProductException.categoryNotActive(request.getCategoryId());
        }

        // Check if category is being changed while product has SKUs
        if (!existingProduct.getCategory().getId().equals(request.getCategoryId()) && 
            !existingProduct.getSkus().isEmpty()) {
            throw ProductException.categoryChangeWithSKUs(
                productId, 
                existingProduct.getCategory().getId(), 
                request.getCategoryId()
            );
        }

        // Check for duplicate name in category (excluding current product)
        if (!existingProduct.getName().equals(request.getName()) &&
            productRepository.existsByNameAndCategoryId(request.getName(), request.getCategoryId())) {
            throw ProductException.duplicateNameInCategory(request.getName(), request.getCategoryId());
        }

        // Validate description requirement
        validateDescription(request);
    }

    private void validateDescription(ProductRequest request) {
        // Validate description requirement for special product types
        boolean isSpecialType = request.getName() != null && 
            (request.getName().toLowerCase().contains("set") ||
             request.getName().toLowerCase().contains("kit") ||
             request.getName().toLowerCase().contains("bundle") ||
             request.getName().toLowerCase().contains("collection"));

        if (isSpecialType && 
            (request.getDescription() == null || request.getDescription().trim().isEmpty())) {
            throw ProductException.invalidDescriptionFormat(
                "Description is required for products with type: set, kit, bundle, or collection");
        }

        // Skip further validation if description is not provided and not required
        if (request.getDescription() == null) {
            return;
        }

        String trimmedDescription = request.getDescription().trim();
        
        // Check for empty or whitespace-only description
        if (trimmedDescription.isEmpty()) {
            if (isSpecialType) {
                throw ProductException.invalidDescriptionFormat(
                    "Description is required for products with type: set, kit, bundle, or collection");
            }
            return;
        }

        // Check for maximum length
        if (trimmedDescription.length() > 1000) {
            throw ProductException.invalidDescriptionFormat(
                "Description cannot exceed 1000 characters");
        }

        // Check for common HTML injection attempts
        if (trimmedDescription.contains("<") || trimmedDescription.contains(">") ||
            trimmedDescription.contains("javascript:") || trimmedDescription.contains("data:")) {
            throw ProductException.invalidDescriptionFormat(
                "HTML tags and potentially harmful content are not allowed");
        }

        // Check for potential SQL injection patterns with case-insensitive check
        String lowerDesc = trimmedDescription.toLowerCase();
        if (lowerDesc.contains("select") ||
            lowerDesc.contains("insert") ||
            lowerDesc.contains("update") ||
            lowerDesc.contains("delete") ||
            lowerDesc.contains("drop") ||
            lowerDesc.contains("union") ||
            trimmedDescription.contains("'") ||
            trimmedDescription.contains("\"") ||
            trimmedDescription.contains(";")) {
            throw ProductException.invalidDescriptionFormat(
                "Invalid characters or SQL keywords detected");
        }

        // Check for reasonable word count and content
        String[] words = trimmedDescription.split("\\s+");
        if (words.length < 3) {
            throw ProductException.invalidDescriptionFormat(
                "Description must contain at least 3 words");
        }

        // Check for repeated characters (potential spam)
        if (hasRepeatedCharacters(trimmedDescription)) {
            throw ProductException.invalidDescriptionFormat(
                "Description contains excessive repeated characters");
        }
    }

    private boolean hasRepeatedCharacters(String text) {
        if (text.length() < 5) return false;
        
        char prevChar = text.charAt(0);
        int repeatCount = 1;
        int maxAllowedRepeat = 4;
        
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) == prevChar) {
                repeatCount++;
                if (repeatCount > maxAllowedRepeat) {
                    return true;
                }
            } else {
                repeatCount = 1;
                prevChar = text.charAt(i);
            }
        }
        return false;
    }

    public void validateProductDeletion(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if (!product.getSkus().isEmpty()) {
            throw ProductException.hasActiveSKUs(productId);
        }
    }
} 
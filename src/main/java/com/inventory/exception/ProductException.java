package com.inventory.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductException extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public ProductException(String message, String code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public static ProductException duplicateNameInCategory(String name, Long categoryId) {
        return new ProductException(
            String.format("Product with name '%s' already exists in category ID: %d", name, categoryId),
            "PROD_001",
            HttpStatus.CONFLICT
        );
    }

    public static ProductException categoryNotActive(Long categoryId) {
        return new ProductException(
            String.format("Cannot create product. Category ID: %d is not active", categoryId),
            "PROD_002",
            HttpStatus.BAD_REQUEST
        );
    }

    public static ProductException hasActiveSKUs(Long productId) {
        return new ProductException(
            String.format("Cannot delete product ID: %d. It has active SKUs", productId),
            "PROD_003",
            HttpStatus.CONFLICT
        );
    }

    public static ProductException categoryChangeWithSKUs(Long productId, Long oldCategoryId, Long newCategoryId) {
        return new ProductException(
            String.format("Cannot change category for product ID: %d from %d to %d. Product has active SKUs", 
                productId, oldCategoryId, newCategoryId),
            "PROD_004",
            HttpStatus.CONFLICT
        );
    }

    public static ProductException invalidDescriptionFormat(String reason) {
        return new ProductException(
            String.format("Invalid product description format: %s", reason),
            "PROD_005",
            HttpStatus.BAD_REQUEST
        );
    }
} 
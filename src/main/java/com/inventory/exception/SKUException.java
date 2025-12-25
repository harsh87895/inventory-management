package com.inventory.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

@Getter
public class SKUException extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public SKUException(String message, String code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public static SKUException duplicateColorAndSize(String color, String size, Long productId) {
        return new SKUException(
            String.format("SKU with color '%s' and size '%s' already exists for product ID: %d", color, size, productId),
            "SKU_001",
            HttpStatus.CONFLICT
        );
    }

    public static SKUException insufficientStock(Long skuId, Integer requested, Integer available) {
        return new SKUException(
            String.format("Insufficient stock for SKU ID: %d. Requested: %d, Available: %d", skuId, requested, available),
            "SKU_002",
            HttpStatus.BAD_REQUEST
        );
    }

    public static SKUException invalidPriceUpdate(Long skuId, BigDecimal oldPrice, BigDecimal newPrice) {
        return new SKUException(
            String.format("Invalid price update for SKU ID: %d. Price cannot be reduced by more than 50%%. Old price: %.2f, New price: %.2f", 
                skuId, oldPrice, newPrice),
            "SKU_003",
            HttpStatus.BAD_REQUEST
        );
    }
} 
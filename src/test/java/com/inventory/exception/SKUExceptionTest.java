package com.inventory.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SKUExceptionTest {

    @Test
    void testConstructor() {
        String message = "Test message";
        String code = "TEST_001";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        SKUException exception = new SKUException(message, code, status);

        assertEquals(message, exception.getMessage());
        assertEquals(code, exception.getCode());
        assertEquals(status, exception.getStatus());
    }

    @Test
    void duplicateColorAndSize() {
        String color = "Black";
        String size = "M";
        Long productId = 1L;

        SKUException exception = SKUException.duplicateColorAndSize(color, size, productId);

        assertEquals("SKU with color 'Black' and size 'M' already exists for product ID: 1", exception.getMessage());
        assertEquals("SKU_001", exception.getCode());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    void insufficientStock() {
        Long skuId = 1L;
        Integer requested = 10;
        Integer available = 5;

        SKUException exception = SKUException.insufficientStock(skuId, requested, available);

        assertEquals("Insufficient stock for SKU ID: 1. Requested: 10, Available: 5", exception.getMessage());
        assertEquals("SKU_002", exception.getCode());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void invalidPriceUpdate() {
        Long skuId = 1L;
        BigDecimal oldPrice = BigDecimal.valueOf(100.00);
        BigDecimal newPrice = BigDecimal.valueOf(40.00);

        SKUException exception = SKUException.invalidPriceUpdate(skuId, oldPrice, newPrice);

        assertEquals("Invalid price update for SKU ID: 1. Price cannot be reduced by more than 50%. Old price: 100.00, New price: 40.00", 
                exception.getMessage());
        assertEquals("SKU_003", exception.getCode());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
} 
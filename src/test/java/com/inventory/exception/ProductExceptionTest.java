package com.inventory.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ProductExceptionTest {

    @Test
    void testConstructor() {
        String message = "Test message";
        String code = "TEST_001";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ProductException exception = new ProductException(message, code, status);

        assertEquals(message, exception.getMessage());
        assertEquals(code, exception.getCode());
        assertEquals(status, exception.getStatus());
    }

    @Test
    void duplicateNameInCategory() {
        String name = "Test Product";
        Long categoryId = 1L;

        ProductException exception = ProductException.duplicateNameInCategory(name, categoryId);

        assertEquals("Product with name 'Test Product' already exists in category ID: 1", exception.getMessage());
        assertEquals("PROD_001", exception.getCode());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    void categoryNotActive() {
        Long categoryId = 1L;

        ProductException exception = ProductException.categoryNotActive(categoryId);

        assertEquals("Cannot create product. Category ID: 1 is not active", exception.getMessage());
        assertEquals("PROD_002", exception.getCode());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void hasActiveSKUs() {
        Long productId = 1L;

        ProductException exception = ProductException.hasActiveSKUs(productId);

        assertEquals("Cannot delete product ID: 1. It has active SKUs", exception.getMessage());
        assertEquals("PROD_003", exception.getCode());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    void categoryChangeWithSKUs() {
        Long productId = 1L;
        Long oldCategoryId = 2L;
        Long newCategoryId = 3L;

        ProductException exception = ProductException.categoryChangeWithSKUs(productId, oldCategoryId, newCategoryId);

        assertEquals("Cannot change category for product ID: 1 from 2 to 3. Product has active SKUs", exception.getMessage());
        assertEquals("PROD_004", exception.getCode());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    void invalidDescriptionFormat() {
        String reason = "Description too long";

        ProductException exception = ProductException.invalidDescriptionFormat(reason);

        assertEquals("Invalid product description format: Description too long", exception.getMessage());
        assertEquals("PROD_005", exception.getCode());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
} 
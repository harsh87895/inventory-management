package com.inventory.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void testMessageConstructor() {
        String message = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testDetailedConstructor() {
        String resourceName = "Product";
        String fieldName = "id";
        Object fieldValue = 1L;

        ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

        assertEquals("Product not found with id: 1", exception.getMessage());
    }

    @Test
    void testDetailedConstructorWithDifferentTypes() {
        // Test with String value
        ResourceNotFoundException exception1 = new ResourceNotFoundException("Category", "name", "Electronics");
        assertEquals("Category not found with name: Electronics", exception1.getMessage());

        // Test with Integer value
        ResourceNotFoundException exception2 = new ResourceNotFoundException("SKU", "stockQuantity", 0);
        assertEquals("SKU not found with stockQuantity: 0", exception2.getMessage());

        // Test with null value
        ResourceNotFoundException exception3 = new ResourceNotFoundException("Product", "category", null);
        assertEquals("Product not found with category: null", exception3.getMessage());
    }
} 
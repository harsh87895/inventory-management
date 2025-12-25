package com.inventory.dto.response;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void errorResponse_Builder() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> validationErrors = new HashMap<>();
        validationErrors.put("field1", "error1");

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(now)
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .path("/api/v1/products")
                .validationErrors(validationErrors)
                .build();

        assertEquals(now, response.getTimestamp());
        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getError());
        assertEquals("Validation failed", response.getMessage());
        assertEquals("/api/v1/products", response.getPath());
        assertEquals(validationErrors, response.getValidationErrors());
    }

    @Test
    void errorResponse_EqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> validationErrors1 = new HashMap<>();
        validationErrors1.put("field1", "error1");

        ErrorResponse response1 = ErrorResponse.builder()
                .timestamp(now)
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .path("/api/v1/products")
                .validationErrors(validationErrors1)
                .build();

        ErrorResponse response2 = ErrorResponse.builder()
                .timestamp(now)
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .path("/api/v1/products")
                .validationErrors(validationErrors1)
                .build();

        Map<String, String> validationErrors2 = new HashMap<>();
        validationErrors2.put("field2", "error2");

        ErrorResponse response3 = ErrorResponse.builder()
                .timestamp(now)
                .status(500)
                .error("Internal Server Error")
                .message("System error")
                .path("/api/v1/categories")
                .validationErrors(validationErrors2)
                .build();

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
        assertNotEquals(response1.hashCode(), response3.hashCode());

        // Test individual field equality
        ErrorResponse response4 = ErrorResponse.builder()
                .timestamp(now)
                .status(400)
                .error("Different Error")
                .message("Validation failed")
                .path("/api/v1/products")
                .validationErrors(validationErrors1)
                .build();
        assertNotEquals(response1, response4);

        // Test with null fields
        ErrorResponse response5 = ErrorResponse.builder()
                .timestamp(now)
                .status(400)
                .error(null)
                .message("Validation failed")
                .path("/api/v1/products")
                .validationErrors(validationErrors1)
                .build();
        assertNotEquals(response1, response5);

        // Test with different object types
        assertNotEquals(response1, new Object());
        assertNotEquals(response1, null);
    }

    @Test
    void errorResponse_ToString() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> validationErrors = new HashMap<>();
        validationErrors.put("field1", "error1");

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(now)
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .path("/api/v1/products")
                .validationErrors(validationErrors)
                .build();

        String toString = response.toString();
        assertTrue(toString.contains("timestamp=" + now));
        assertTrue(toString.contains("status=400"));
        assertTrue(toString.contains("error=Bad Request"));
        assertTrue(toString.contains("message=Validation failed"));
        assertTrue(toString.contains("path=/api/v1/products"));
        assertTrue(toString.contains("validationErrors={field1=error1}"));

        // Test with null values
        ErrorResponse nullResponse = ErrorResponse.builder()
                .timestamp(null)
                .status(0)
                .error(null)
                .message(null)
                .path(null)
                .validationErrors(null)
                .build();

        String nullToString = nullResponse.toString();
        assertTrue(nullToString.contains("timestamp=null"));
        assertTrue(nullToString.contains("status=0"));
        assertTrue(nullToString.contains("error=null"));
        assertTrue(nullToString.contains("message=null"));
        assertTrue(nullToString.contains("path=null"));
        assertTrue(nullToString.contains("validationErrors=null"));
    }
} 
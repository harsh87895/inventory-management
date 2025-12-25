package com.inventory.dto.request;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RequestDTOTest {

    @Test
    void categoryRequest_EqualsAndHashCode() {
        CategoryRequest request1 = CategoryRequest.builder()
                .name("Electronics")
                .build();

        CategoryRequest request2 = CategoryRequest.builder()
                .name("Electronics")
                .build();

        CategoryRequest request3 = CategoryRequest.builder()
                .name("Books")
                .build();

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void productRequest_EqualsAndHashCode() {
        ProductRequest request1 = ProductRequest.builder()
                .name("Laptop")
                .description("High-end laptop")
                .categoryId(1L)
                .build();

        ProductRequest request2 = ProductRequest.builder()
                .name("Laptop")
                .description("High-end laptop")
                .categoryId(1L)
                .build();

        ProductRequest request3 = ProductRequest.builder()
                .name("Book")
                .description("Fiction book")
                .categoryId(2L)
                .build();

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void skuRequest_EqualsAndHashCode() {
        SKURequest request1 = SKURequest.builder()
                .productId(1L)
                .color("Black")
                .size("15-inch")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(10)
                .build();

        SKURequest request2 = SKURequest.builder()
                .productId(1L)
                .color("Black")
                .size("15-inch")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(10)
                .build();

        SKURequest request3 = SKURequest.builder()
                .productId(1L)
                .color("Silver")
                .size("13-inch")
                .price(BigDecimal.valueOf(799.99))
                .stockQuantity(5)
                .build();

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void categoryRequest_ToString() {
        CategoryRequest request = CategoryRequest.builder()
                .name("Electronics")
                .build();

        String toString = request.toString();
        assertTrue(toString.contains("name=Electronics"));
    }

    @Test
    void productRequest_ToString() {
        ProductRequest request = ProductRequest.builder()
                .name("Laptop")
                .description("High-end laptop")
                .categoryId(1L)
                .build();

        String toString = request.toString();
        assertTrue(toString.contains("name=Laptop"));
        assertTrue(toString.contains("description=High-end laptop"));
        assertTrue(toString.contains("categoryId=1"));
    }

    @Test
    void skuRequest_ToString() {
        SKURequest request = SKURequest.builder()
                .productId(1L)
                .color("Black")
                .size("15-inch")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(10)
                .build();

        String toString = request.toString();
        assertTrue(toString.contains("productId=1"));
        assertTrue(toString.contains("color=Black"));
        assertTrue(toString.contains("size=15-inch"));
        assertTrue(toString.contains("price=999.99"));
        assertTrue(toString.contains("stockQuantity=10"));
    }

    @Test
    void categoryRequest_NoArgsConstructor() {
        CategoryRequest request = new CategoryRequest();
        assertNotNull(request);
        assertNull(request.getName());
    }

    @Test
    void productRequest_NoArgsConstructor() {
        ProductRequest request = new ProductRequest();
        assertNotNull(request);
        assertNull(request.getName());
        assertNull(request.getDescription());
        assertNull(request.getCategoryId());
    }

    @Test
    void skuRequest_NoArgsConstructor() {
        SKURequest request = new SKURequest();
        assertNotNull(request);
        assertNull(request.getProductId());
        assertNull(request.getColor());
        assertNull(request.getSize());
        assertNull(request.getPrice());
        assertNull(request.getStockQuantity());
    }
} 
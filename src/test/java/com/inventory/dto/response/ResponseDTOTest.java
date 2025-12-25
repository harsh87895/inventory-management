package com.inventory.dto.response;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ResponseDTOTest {

    @Test
    void categoryResponse_EqualsAndHashCode() {
        CategoryResponse response1 = CategoryResponse.builder()
                .id(1L)
                .name("Electronics")
                .productCount(5)
                .build();

        CategoryResponse response2 = CategoryResponse.builder()
                .id(1L)
                .name("Electronics")
                .productCount(5)
                .build();

        CategoryResponse response3 = CategoryResponse.builder()
                .id(2L)
                .name("Books")
                .productCount(3)
                .build();

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
        assertNotEquals(response1.hashCode(), response3.hashCode());

        // Test individual field equality
        CategoryResponse response4 = CategoryResponse.builder()
                .id(1L)
                .name("Different")
                .productCount(5)
                .build();
        assertNotEquals(response1, response4);

        CategoryResponse response5 = CategoryResponse.builder()
                .id(1L)
                .name("Electronics")
                .productCount(10)
                .build();
        assertNotEquals(response1, response5);

        // Test null fields
        CategoryResponse response6 = CategoryResponse.builder()
                .id(1L)
                .name(null)
                .productCount(5)
                .build();
        assertNotEquals(response1, response6);

        // Test with different object types
        assertNotEquals(response1, new Object());
        assertNotEquals(response1, null);
    }

    @Test
    void productResponse_EqualsAndHashCode() {
        ProductResponse response1 = ProductResponse.builder()
                .id(1L)
                .name("Laptop")
                .description("High-end laptop")
                .categoryId(1L)
                .categoryName("Electronics")
                .skuCount(3)
                .build();

        ProductResponse response2 = ProductResponse.builder()
                .id(1L)
                .name("Laptop")
                .description("High-end laptop")
                .categoryId(1L)
                .categoryName("Electronics")
                .skuCount(3)
                .build();

        ProductResponse response3 = ProductResponse.builder()
                .id(2L)
                .name("Book")
                .description("Fiction book")
                .categoryId(2L)
                .categoryName("Books")
                .skuCount(1)
                .build();

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
        assertNotEquals(response1.hashCode(), response3.hashCode());

        // Test individual field equality
        ProductResponse response4 = ProductResponse.builder()
                .id(1L)
                .name("Different")
                .description("High-end laptop")
                .categoryId(1L)
                .categoryName("Electronics")
                .skuCount(3)
                .build();
        assertNotEquals(response1, response4);

        ProductResponse response5 = ProductResponse.builder()
                .id(1L)
                .name("Laptop")
                .description("Different")
                .categoryId(1L)
                .categoryName("Electronics")
                .skuCount(3)
                .build();
        assertNotEquals(response1, response5);

        ProductResponse response6 = ProductResponse.builder()
                .id(1L)
                .name("Laptop")
                .description("High-end laptop")
                .categoryId(2L)
                .categoryName("Electronics")
                .skuCount(3)
                .build();
        assertNotEquals(response1, response6);

        // Test null fields
        ProductResponse response7 = ProductResponse.builder()
                .id(1L)
                .name(null)
                .description("High-end laptop")
                .categoryId(1L)
                .categoryName("Electronics")
                .skuCount(3)
                .build();
        assertNotEquals(response1, response7);

        // Test with different object types
        assertNotEquals(response1, new Object());
        assertNotEquals(response1, null);
    }

    @Test
    void skuResponse_EqualsAndHashCode() {
        SKUResponse response1 = SKUResponse.builder()
                .id(1L)
                .productId(1L)
                .productName("Laptop")
                .color("Black")
                .size("15-inch")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(10)
                .build();

        SKUResponse response2 = SKUResponse.builder()
                .id(1L)
                .productId(1L)
                .productName("Laptop")
                .color("Black")
                .size("15-inch")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(10)
                .build();

        SKUResponse response3 = SKUResponse.builder()
                .id(2L)
                .productId(1L)
                .productName("Laptop")
                .color("Silver")
                .size("13-inch")
                .price(BigDecimal.valueOf(799.99))
                .stockQuantity(5)
                .build();

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
        assertNotEquals(response1.hashCode(), response3.hashCode());

        // Test individual field equality
        SKUResponse response4 = SKUResponse.builder()
                .id(1L)
                .productId(2L)
                .productName("Laptop")
                .color("Black")
                .size("15-inch")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(10)
                .build();
        assertNotEquals(response1, response4);

        SKUResponse response5 = SKUResponse.builder()
                .id(1L)
                .productId(1L)
                .productName("Different")
                .color("Black")
                .size("15-inch")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(10)
                .build();
        assertNotEquals(response1, response5);

        SKUResponse response6 = SKUResponse.builder()
                .id(1L)
                .productId(1L)
                .productName("Laptop")
                .color("Different")
                .size("15-inch")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(10)
                .build();
        assertNotEquals(response1, response6);

        SKUResponse response7 = SKUResponse.builder()
                .id(1L)
                .productId(1L)
                .productName("Laptop")
                .color("Black")
                .size("Different")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(10)
                .build();
        assertNotEquals(response1, response7);

        SKUResponse response8 = SKUResponse.builder()
                .id(1L)
                .productId(1L)
                .productName("Laptop")
                .color("Black")
                .size("15-inch")
                .price(BigDecimal.valueOf(888.88))
                .stockQuantity(10)
                .build();
        assertNotEquals(response1, response8);

        SKUResponse response9 = SKUResponse.builder()
                .id(1L)
                .productId(1L)
                .productName("Laptop")
                .color("Black")
                .size("15-inch")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(20)
                .build();
        assertNotEquals(response1, response9);

        // Test null fields
        SKUResponse response10 = SKUResponse.builder()
                .id(1L)
                .productId(1L)
                .productName(null)
                .color("Black")
                .size("15-inch")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(10)
                .build();
        assertNotEquals(response1, response10);

        // Test with different object types
        assertNotEquals(response1, new Object());
        assertNotEquals(response1, null);
    }

    @Test
    void categoryResponse_ToString() {
        CategoryResponse response = CategoryResponse.builder()
                .id(1L)
                .name("Electronics")
                .productCount(5)
                .build();

        String toString = response.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=Electronics"));
        assertTrue(toString.contains("productCount=5"));

        // Test with null values
        CategoryResponse nullResponse = CategoryResponse.builder()
                .id(null)
                .name(null)
                .productCount(0)
                .build();
        String nullToString = nullResponse.toString();
        assertTrue(nullToString.contains("id=null"));
        assertTrue(nullToString.contains("name=null"));
        assertTrue(nullToString.contains("productCount=0"));
    }

    @Test
    void productResponse_ToString() {
        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .name("Laptop")
                .description("High-end laptop")
                .categoryId(1L)
                .categoryName("Electronics")
                .skuCount(3)
                .build();

        String toString = response.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=Laptop"));
        assertTrue(toString.contains("description=High-end laptop"));
        assertTrue(toString.contains("categoryId=1"));
        assertTrue(toString.contains("categoryName=Electronics"));
        assertTrue(toString.contains("skuCount=3"));

        // Test with null values
        ProductResponse nullResponse = ProductResponse.builder()
                .id(null)
                .name(null)
                .description(null)
                .categoryId(null)
                .categoryName(null)
                .skuCount(0)
                .build();
        String nullToString = nullResponse.toString();
        assertTrue(nullToString.contains("id=null"));
        assertTrue(nullToString.contains("name=null"));
        assertTrue(nullToString.contains("description=null"));
        assertTrue(nullToString.contains("categoryId=null"));
        assertTrue(nullToString.contains("categoryName=null"));
        assertTrue(nullToString.contains("skuCount=0"));
    }

    @Test
    void skuResponse_ToString() {
        SKUResponse response = SKUResponse.builder()
                .id(1L)
                .productId(1L)
                .productName("Laptop")
                .color("Black")
                .size("15-inch")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(10)
                .build();

        String toString = response.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("productId=1"));
        assertTrue(toString.contains("productName=Laptop"));
        assertTrue(toString.contains("color=Black"));
        assertTrue(toString.contains("size=15-inch"));
        assertTrue(toString.contains("price=999.99"));
        assertTrue(toString.contains("stockQuantity=10"));

        // Test with null values
        SKUResponse nullResponse = SKUResponse.builder()
                .id(null)
                .productId(null)
                .productName(null)
                .color(null)
                .size(null)
                .price(null)
                .stockQuantity(null)
                .build();
        String nullToString = nullResponse.toString();
        assertTrue(nullToString.contains("id=null"));
        assertTrue(nullToString.contains("productId=null"));
        assertTrue(nullToString.contains("productName=null"));
        assertTrue(nullToString.contains("color=null"));
        assertTrue(nullToString.contains("size=null"));
        assertTrue(nullToString.contains("price=null"));
        assertTrue(nullToString.contains("stockQuantity=null"));
    }

    @Test
    void categoryResponse_NoArgsConstructor() {
        CategoryResponse response = new CategoryResponse();
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getName());
        assertEquals(0, response.getProductCount());

        // Test setters
        response.setId(1L);
        response.setName("Electronics");
        response.setProductCount(5);

        assertEquals(1L, response.getId());
        assertEquals("Electronics", response.getName());
        assertEquals(5, response.getProductCount());
    }

    @Test
    void productResponse_NoArgsConstructor() {
        ProductResponse response = new ProductResponse();
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getName());
        assertNull(response.getDescription());
        assertNull(response.getCategoryId());
        assertNull(response.getCategoryName());
        assertEquals(0, response.getSkuCount());

        // Test setters
        response.setId(1L);
        response.setName("Laptop");
        response.setDescription("High-end laptop");
        response.setCategoryId(1L);
        response.setCategoryName("Electronics");
        response.setSkuCount(3);

        assertEquals(1L, response.getId());
        assertEquals("Laptop", response.getName());
        assertEquals("High-end laptop", response.getDescription());
        assertEquals(1L, response.getCategoryId());
        assertEquals("Electronics", response.getCategoryName());
        assertEquals(3, response.getSkuCount());
    }

    @Test
    void skuResponse_NoArgsConstructor() {
        SKUResponse response = new SKUResponse();
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getProductId());
        assertNull(response.getProductName());
        assertNull(response.getColor());
        assertNull(response.getSize());
        assertNull(response.getPrice());
        assertNull(response.getStockQuantity());

        // Test setters
        response.setId(1L);
        response.setProductId(1L);
        response.setProductName("Laptop");
        response.setColor("Black");
        response.setSize("15-inch");
        response.setPrice(BigDecimal.valueOf(999.99));
        response.setStockQuantity(10);

        assertEquals(1L, response.getId());
        assertEquals(1L, response.getProductId());
        assertEquals("Laptop", response.getProductName());
        assertEquals("Black", response.getColor());
        assertEquals("15-inch", response.getSize());
        assertEquals(BigDecimal.valueOf(999.99), response.getPrice());
        assertEquals(10, response.getStockQuantity());
    }
} 
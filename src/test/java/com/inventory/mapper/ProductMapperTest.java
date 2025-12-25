package com.inventory.mapper;

import com.inventory.dto.request.ProductRequest;
import com.inventory.dto.response.ProductResponse;
import com.inventory.model.Category;
import com.inventory.model.Product;
import com.inventory.model.SKU;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper mapper;
    private Category category;
    private ProductRequest request;

    @BeforeEach
    void setUp() {
        mapper = new ProductMapper();
        
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setActive(true);

        request = ProductRequest.builder()
                .name("Test Product")
                .description("Test Description")
                .categoryId(1L)
                .build();
    }

    @Test
    void toEntity_WithValidRequest_ShouldMapAllFields() {
        Product product = mapper.toEntity(request, category);

        assertNotNull(product);
        assertEquals(request.getName(), product.getName());
        assertEquals(request.getDescription(), product.getDescription());
        assertEquals(category, product.getCategory());
        assertTrue(product.getSkus().isEmpty());
    }

    @Test
    void updateEntity_WithValidRequest_ShouldUpdateAllFields() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Old Name");
        product.setDescription("Old Description");
        product.setCategory(category);
        product.setSkus(new ArrayList<>());

        mapper.updateEntity(product, request, category);

        assertEquals(request.getName(), product.getName());
        assertEquals(request.getDescription(), product.getDescription());
        assertEquals(category, product.getCategory());
        assertEquals(1L, product.getId());
        assertTrue(product.getSkus().isEmpty());
    }

    @Test
    void toResponse_WithValidProduct_ShouldMapAllFields() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setCategory(category);
        
        List<SKU> skus = new ArrayList<>();
        skus.add(new SKU());
        skus.add(new SKU());
        product.setSkus(skus);

        ProductResponse response = mapper.toResponse(product);

        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getCategory().getId(), response.getCategoryId());
        assertEquals(product.getCategory().getName(), response.getCategoryName());
        assertEquals(2, response.getSkuCount());
    }

    @Test
    void toResponse_WithNullValues_ShouldHandleGracefully() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setCategory(category);
        product.setSkus(null);

        ProductResponse response = mapper.toResponse(product);

        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getName(), response.getName());
        assertNull(response.getDescription());
        assertEquals(product.getCategory().getId(), response.getCategoryId());
        assertEquals(product.getCategory().getName(), response.getCategoryName());
        assertEquals(0, response.getSkuCount());
    }
} 
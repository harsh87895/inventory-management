package com.inventory.controller;

import com.inventory.dto.request.ProductRequest;
import com.inventory.dto.response.ProductResponse;
import com.inventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productRequest = ProductRequest.builder()
                .name("Test Product")
                .description("Test Description")
                .categoryId(1L)
                .build();

        productResponse = ProductResponse.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .categoryId(1L)
                .categoryName("Electronics")
                .skuCount(0)
                .build();
    }

    @Test
    void createProduct_Success() {
        when(productService.createProduct(any(ProductRequest.class)))
                .thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.createProduct(productRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(productResponse, response.getBody());
        verify(productService).createProduct(productRequest);
    }

    @Test
    void getProductById_Success() {
        when(productService.getProductById(1L)).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productResponse, response.getBody());
        verify(productService).getProductById(1L);
    }

    @Test
    void searchProducts_Success() {
        Page<ProductResponse> page = new PageImpl<>(List.of(productResponse));
        when(productService.searchProducts(eq(1L), eq("Test"), any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<ProductResponse>> response = productController.searchProducts(1L, "Test", PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
        verify(productService).searchProducts(eq(1L), eq("Test"), any(PageRequest.class));
    }

    @Test
    void searchProducts_NoFilters_Success() {
        Page<ProductResponse> page = new PageImpl<>(List.of(productResponse));
        when(productService.searchProducts(eq(null), eq(null), any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<ProductResponse>> response = productController.searchProducts(null, null, PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
        verify(productService).searchProducts(eq(null), eq(null), any(PageRequest.class));
    }

    @Test
    void updateProduct_Success() {
        when(productService.updateProduct(eq(1L), any(ProductRequest.class)))
                .thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.updateProduct(1L, productRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productResponse, response.getBody());
        verify(productService).updateProduct(1L, productRequest);
    }

    @Test
    void deleteProduct_Success() {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService).deleteProduct(1L);
    }
} 
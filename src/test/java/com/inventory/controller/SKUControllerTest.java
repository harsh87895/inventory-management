package com.inventory.controller;

import com.inventory.dto.request.SKURequest;
import com.inventory.dto.response.SKUResponse;
import com.inventory.service.SKUService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SKUControllerTest {

    @Mock
    private SKUService skuService;

    @InjectMocks
    private SKUController skuController;

    private SKURequest skuRequest;
    private SKUResponse skuResponse;

    @BeforeEach
    void setUp() {
        skuRequest = SKURequest.builder()
                .productId(1L)
                .color("Red")
                .size("M")
                .price(BigDecimal.valueOf(29.99))
                .stockQuantity(100)
                .build();

        skuResponse = SKUResponse.builder()
                .id(1L)
                .productId(1L)
                .productName("Test Product")
                .color("Red")
                .size("M")
                .price(BigDecimal.valueOf(29.99))
                .stockQuantity(100)
                .build();
    }

    @Test
    void createSKU_Success() {
        when(skuService.createSKU(any(SKURequest.class)))
                .thenReturn(skuResponse);

        ResponseEntity<SKUResponse> response = skuController.createSKU(skuRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(skuResponse, response.getBody());
        verify(skuService).createSKU(skuRequest);
    }

    @Test
    void getSKUById_Success() {
        when(skuService.getSKUById(1L)).thenReturn(skuResponse);

        ResponseEntity<SKUResponse> response = skuController.getSKUById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(skuResponse, response.getBody());
        verify(skuService).getSKUById(1L);
    }

    @Test
    void getSKUsByProductId_Success() {
        Page<SKUResponse> page = new PageImpl<>(List.of(skuResponse));
        when(skuService.getSKUsByProductId(eq(1L), any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<SKUResponse>> response = skuController.getSKUsByProductId(1L, PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
        verify(skuService).getSKUsByProductId(eq(1L), any(PageRequest.class));
    }

    @Test
    void updateSKU_Success() {
        when(skuService.updateSKU(eq(1L), any(SKURequest.class)))
                .thenReturn(skuResponse);

        ResponseEntity<SKUResponse> response = skuController.updateSKU(1L, skuRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(skuResponse, response.getBody());
        verify(skuService).updateSKU(1L, skuRequest);
    }

    @Test
    void deleteSKU_Success() {
        doNothing().when(skuService).deleteSKU(1L);

        ResponseEntity<Void> response = skuController.deleteSKU(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(skuService).deleteSKU(1L);
    }
} 
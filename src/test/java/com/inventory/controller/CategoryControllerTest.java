package com.inventory.controller;

import com.inventory.dto.request.CategoryRequest;
import com.inventory.dto.response.CategoryResponse;
import com.inventory.service.CategoryService;
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
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Electronics");

        categoryResponse = CategoryResponse.builder()
                .id(1L)
                .name("Electronics")
                .productCount(0)
                .build();
    }

    @Test
    void createCategory_Success() {
        when(categoryService.createCategory(any(CategoryRequest.class)))
                .thenReturn(categoryResponse);

        ResponseEntity<CategoryResponse> response = categoryController.createCategory(categoryRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(categoryResponse, response.getBody());
        verify(categoryService).createCategory(categoryRequest);
    }

    @Test
    void getCategoryById_Success() {
        when(categoryService.getCategoryById(1L)).thenReturn(categoryResponse);

        ResponseEntity<CategoryResponse> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryResponse, response.getBody());
        verify(categoryService).getCategoryById(1L);
    }

    @Test
    void getAllCategories_Success() {
        Page<CategoryResponse> page = new PageImpl<>(List.of(categoryResponse));
        when(categoryService.getAllCategories(any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<CategoryResponse>> response = categoryController.getAllCategories(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
        verify(categoryService).getAllCategories(any(PageRequest.class));
    }

    @Test
    void updateCategory_Success() {
        when(categoryService.updateCategory(eq(1L), any(CategoryRequest.class)))
                .thenReturn(categoryResponse);

        ResponseEntity<CategoryResponse> response = categoryController.updateCategory(1L, categoryRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryResponse, response.getBody());
        verify(categoryService).updateCategory(1L, categoryRequest);
    }

    @Test
    void deleteCategory_Success() {
        doNothing().when(categoryService).deleteCategory(1L);

        ResponseEntity<Void> response = categoryController.deleteCategory(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoryService).deleteCategory(1L);
    }
} 
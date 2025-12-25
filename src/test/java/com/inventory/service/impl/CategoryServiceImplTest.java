package com.inventory.service.impl;

import com.inventory.dto.request.CategoryRequest;
import com.inventory.dto.response.CategoryResponse;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.model.Category;
import com.inventory.model.Product;
import com.inventory.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequest validRequest;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        // Set up test data
        validRequest = CategoryRequest.builder()
            .name("Test Category")
            .build();

        category = new Category();
        category.setId(1L);
        category.setName(validRequest.getName());
        
        products = new ArrayList<>();
        category.setProducts(products);
    }

    @Test
    @DisplayName("Should successfully create a category when input is valid")
    void createCategory_WithValidInput_ShouldCreateCategory() {
        // Arrange
        when(categoryRepository.existsByName(validRequest.getName())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        CategoryResponse response = categoryService.createCategory(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals(category.getId(), response.getId());
        assertEquals(category.getName(), response.getName());
        assertEquals(0, response.getProductCount());

        verify(categoryRepository).existsByName(validRequest.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Should throw exception when creating category with duplicate name")
    void createCategory_WithDuplicateName_ShouldThrowException() {
        // Arrange
        when(categoryRepository.existsByName(validRequest.getName())).thenReturn(true);

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
            DataIntegrityViolationException.class,
            () -> categoryService.createCategory(validRequest)
        );

        assertEquals("Category with name Test Category already exists", exception.getMessage());
        verify(categoryRepository).existsByName(validRequest.getName());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Should successfully update a category when input is valid")
    void updateCategory_WithValidInput_ShouldUpdateCategory() {
        // Arrange
        Long categoryId = 1L;
        CategoryRequest updateRequest = CategoryRequest.builder()
            .name("Updated Category")
            .build();

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Test Category");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName(updateRequest.getName())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        // Act
        CategoryResponse response = categoryService.updateCategory(categoryId, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(categoryId, response.getId());
        assertEquals(updateRequest.getName(), response.getName());

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).existsByName(updateRequest.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent category")
    void updateCategory_WithNonExistentId_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        when(categoryRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> categoryService.updateCategory(nonExistentId, validRequest)
        );

        assertEquals("Category not found with id: 999", exception.getMessage());
        verify(categoryRepository).findById(nonExistentId);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Should successfully get category by ID")
    void getCategoryById_WithValidId_ShouldReturnCategory() {
        // Arrange
        Long categoryId = 1L;
        when(categoryRepository.findByIdWithProducts(categoryId)).thenReturn(Optional.of(category));

        // Act
        CategoryResponse response = categoryService.getCategoryById(categoryId);

        // Assert
        assertNotNull(response);
        assertEquals(category.getId(), response.getId());
        assertEquals(category.getName(), response.getName());
        assertEquals(category.getProducts().size(), response.getProductCount());

        verify(categoryRepository).findByIdWithProducts(categoryId);
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent category")
    void getCategoryById_WithNonExistentId_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        when(categoryRepository.findByIdWithProducts(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> categoryService.getCategoryById(nonExistentId)
        );

        assertEquals("Category not found with id: 999", exception.getMessage());
        verify(categoryRepository).findByIdWithProducts(nonExistentId);
    }

    @Test
    @DisplayName("Should successfully get all categories")
    void getAllCategories_ShouldReturnPageOfCategories() {
        // Arrange
        List<Category> categories = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(categories);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        // Act
        Page<CategoryResponse> response = categoryService.getAllCategories(pageable);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(category.getName(), response.getContent().get(0).getName());

        verify(categoryRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should successfully delete category")
    void deleteCategory_WithValidId_ShouldDeleteCategory() {
        // Arrange
        Long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(categoryId);

        // Act
        categoryService.deleteCategory(categoryId);

        // Assert
        verify(categoryRepository).existsById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    @DisplayName("Should throw exception when deleting category with products")
    void deleteCategory_WithProducts_ShouldThrowException() {
        // Arrange
        Long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("FK constraint"))
            .when(categoryRepository).deleteById(categoryId);

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
            DataIntegrityViolationException.class,
            () -> categoryService.deleteCategory(categoryId)
        );

        assertEquals("Cannot delete category. It has associated products.", exception.getMessage());
        verify(categoryRepository).existsById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }
} 
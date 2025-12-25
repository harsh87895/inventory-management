package com.inventory.service.impl;

import com.inventory.dto.request.ProductRequest;
import com.inventory.dto.response.ProductResponse;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.mapper.ProductMapper;
import com.inventory.model.Category;
import com.inventory.model.Product;
import com.inventory.model.SKU;
import com.inventory.repository.CategoryRepository;
import com.inventory.repository.ProductRepository;
import com.inventory.service.validation.ProductValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductValidationService validationService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Category category;
    private ProductRequest validRequest;
    private Product savedProduct;
    private ProductResponse productResponse;
    private List<SKU> skus;

    @BeforeEach
    void setUp() {
        // Set up test data
        category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        validRequest = ProductRequest.builder()
            .name("Test Product")
            .description("Test Description")
            .categoryId(1L)
            .build();

        savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName(validRequest.getName());
        savedProduct.setDescription(validRequest.getDescription());
        savedProduct.setCategory(category);
        
        skus = new ArrayList<>();
        savedProduct.setSkus(skus);

        productResponse = ProductResponse.builder()
            .id(savedProduct.getId())
            .name(savedProduct.getName())
            .description(savedProduct.getDescription())
            .categoryId(category.getId())
            .categoryName(category.getName())
            .skuCount(0)
            .build();
    }

    @Test
    @DisplayName("Should successfully create a product when input is valid")
    void createProduct_WithValidInput_ShouldCreateProduct() {
        // Arrange
        when(categoryRepository.findById(validRequest.getCategoryId()))
            .thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class)))
            .thenReturn(savedProduct);
        when(productMapper.toEntity(any(), any()))
            .thenReturn(savedProduct);
        when(productMapper.toResponse(any()))
            .thenReturn(productResponse);
        doNothing().when(validationService).validateProductCreation(any());

        // Act
        ProductResponse response = productService.createProduct(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals(savedProduct.getId(), response.getId());
        assertEquals(savedProduct.getName(), response.getName());
        assertEquals(savedProduct.getDescription(), response.getDescription());
        assertEquals(savedProduct.getCategory().getId(), response.getCategoryId());
        assertEquals(savedProduct.getCategory().getName(), response.getCategoryName());
        assertEquals(0, response.getSkuCount());

        verify(validationService).validateProductCreation(validRequest);
        verify(categoryRepository).findById(validRequest.getCategoryId());
        verify(productMapper).toEntity(validRequest, category);
        verify(productRepository).save(any(Product.class));
        verify(productMapper).toResponse(savedProduct);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when category does not exist")
    void createProduct_WithInvalidCategory_ShouldThrowException() {
        // Arrange
        doThrow(new ResourceNotFoundException("Category", "id", 1L))
            .when(validationService).validateProductCreation(validRequest);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> productService.createProduct(validRequest)
        );

        assertEquals("Category not found with id: 1", exception.getMessage());
        verify(validationService).validateProductCreation(validRequest);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should successfully get product by ID")
    void getProductById_WithValidId_ShouldReturnProduct() {
        // Arrange
        Long productId = 1L;
        when(productRepository.findByIdWithSkus(productId))
            .thenReturn(Optional.of(savedProduct));
        when(productMapper.toResponse(savedProduct))
            .thenReturn(productResponse);

        // Act
        ProductResponse response = productService.getProductById(productId);

        // Assert
        assertNotNull(response);
        assertEquals(savedProduct.getId(), response.getId());
        assertEquals(savedProduct.getName(), response.getName());
        assertEquals(savedProduct.getDescription(), response.getDescription());
        assertEquals(savedProduct.getCategory().getId(), response.getCategoryId());
        assertEquals(savedProduct.getCategory().getName(), response.getCategoryName());

        verify(productRepository).findByIdWithSkus(productId);
        verify(productMapper).toResponse(savedProduct);
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent product")
    void getProductById_WithNonExistentId_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        when(productRepository.findByIdWithSkus(nonExistentId))
            .thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> productService.getProductById(nonExistentId)
        );

        assertEquals("Product not found with id: 999", exception.getMessage());
        verify(productRepository).findByIdWithSkus(nonExistentId);
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should successfully search products")
    void searchProducts_ShouldReturnFilteredProducts() {
        // Arrange
        Long categoryId = 1L;
        String name = "Test";
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = List.of(savedProduct);
        Page<Product> productPage = new PageImpl<>(products);

        when(productRepository.findByCategoryIdAndNameContaining(categoryId, name, pageable))
            .thenReturn(productPage);
        when(productMapper.toResponse(any()))
            .thenReturn(productResponse);

        // Act
        Page<ProductResponse> response = productService.searchProducts(categoryId, name, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        ProductResponse productResponse = response.getContent().get(0);
        assertEquals(savedProduct.getId(), productResponse.getId());
        assertEquals(savedProduct.getName(), productResponse.getName());

        verify(productRepository).findByCategoryIdAndNameContaining(categoryId, name, pageable);
        verify(productMapper).toResponse(savedProduct);
    }

    @Test
    @DisplayName("Should successfully delete product")
    void deleteProduct_WithValidId_ShouldDeleteProduct() {
        // Arrange
        Long productId = 1L;
        doNothing().when(validationService).validateProductDeletion(productId);
        doNothing().when(productRepository).deleteById(productId);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(validationService).validateProductDeletion(productId);
        verify(productRepository).deleteById(productId);
    }
} 
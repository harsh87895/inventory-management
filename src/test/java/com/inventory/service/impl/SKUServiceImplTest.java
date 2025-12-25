package com.inventory.service.impl;

import com.inventory.dto.request.SKURequest;
import com.inventory.dto.response.SKUResponse;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.model.Category;
import com.inventory.model.Product;
import com.inventory.model.SKU;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SKURepository;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SKUServiceImplTest {

    @Mock
    private SKURepository skuRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SKUServiceImpl skuService;

    private Product product;
    private Category category;
    private SKU sku;
    private SKURequest validRequest;

    @BeforeEach
    void setUp() {
        // Set up test data
        category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setCategory(category);

        validRequest = SKURequest.builder()
            .productId(1L)
            .color("Red")
            .size("M")
            .price(new BigDecimal("29.99"))
            .stockQuantity(100)
            .build();

        sku = new SKU();
        sku.setId(1L);
        sku.setProduct(product);
        sku.setColor(validRequest.getColor());
        sku.setSize(validRequest.getSize());
        sku.setPrice(validRequest.getPrice());
        sku.setStockQuantity(validRequest.getStockQuantity());
    }

    @Test
    @DisplayName("Should successfully create a SKU when input is valid")
    void createSKU_WithValidInput_ShouldCreateSKU() {
        // Arrange
        when(productRepository.findById(validRequest.getProductId()))
            .thenReturn(Optional.of(product));
        when(skuRepository.existsByProductIdAndColorAndSize(
            validRequest.getProductId(), validRequest.getColor(), validRequest.getSize()))
            .thenReturn(false);
        when(skuRepository.save(any(SKU.class)))
            .thenReturn(sku);

        // Act
        SKUResponse response = skuService.createSKU(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals(sku.getId(), response.getId());
        assertEquals(sku.getProduct().getId(), response.getProductId());
        assertEquals(sku.getColor(), response.getColor());
        assertEquals(sku.getSize(), response.getSize());
        assertEquals(sku.getPrice(), response.getPrice());
        assertEquals(sku.getStockQuantity(), response.getStockQuantity());
        assertEquals(sku.getProduct().getCategory().getName(), response.getCategoryName());

        verify(productRepository).findById(validRequest.getProductId());
        verify(skuRepository).existsByProductIdAndColorAndSize(
            validRequest.getProductId(), validRequest.getColor(), validRequest.getSize());
        verify(skuRepository).save(any(SKU.class));
    }

    @Test
    @DisplayName("Should throw exception when creating SKU for non-existent product")
    void createSKU_WithNonExistentProduct_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(validRequest.getProductId()))
            .thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> skuService.createSKU(validRequest)
        );

        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(productRepository).findById(validRequest.getProductId());
        verify(skuRepository, never()).save(any(SKU.class));
    }

    @Test
    @DisplayName("Should throw exception when creating duplicate SKU")
    void createSKU_WithDuplicateColorAndSize_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(validRequest.getProductId()))
            .thenReturn(Optional.of(product));
        when(skuRepository.existsByProductIdAndColorAndSize(
            validRequest.getProductId(), validRequest.getColor(), validRequest.getSize()))
            .thenReturn(true);

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
            DataIntegrityViolationException.class,
            () -> skuService.createSKU(validRequest)
        );

        assertEquals(
            String.format("SKU with color '%s' and size '%s' already exists for this product",
                validRequest.getColor(), validRequest.getSize()),
            exception.getMessage()
        );
        verify(productRepository).findById(validRequest.getProductId());
        verify(skuRepository).existsByProductIdAndColorAndSize(
            validRequest.getProductId(), validRequest.getColor(), validRequest.getSize());
        verify(skuRepository, never()).save(any(SKU.class));
    }

    @Test
    @DisplayName("Should successfully update a SKU when input is valid")
    void updateSKU_WithValidInput_ShouldUpdateSKU() {
        // Arrange
        Long skuId = 1L;
        SKURequest updateRequest = SKURequest.builder()
            .productId(1L)
            .color("Blue")
            .size("L")
            .price(new BigDecimal("39.99"))
            .stockQuantity(50)
            .build();

        when(skuRepository.findByIdWithProductAndCategory(skuId))
            .thenReturn(sku);
        when(skuRepository.save(any(SKU.class)))
            .thenReturn(sku);

        // Act
        SKUResponse response = skuService.updateSKU(skuId, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(skuId, response.getId());
        assertEquals(updateRequest.getProductId(), response.getProductId());
        assertEquals(product.getName(), response.getProductName());
        assertEquals(category.getName(), response.getCategoryName());

        verify(skuRepository).findByIdWithProductAndCategory(skuId);
        verify(skuRepository).save(any(SKU.class));
    }

    @Test
    @DisplayName("Should successfully update SKU with new product")
    void updateSKU_WithNewProduct_ShouldUpdateSKU() {
        // Arrange
        Long skuId = 1L;
        Long newProductId = 2L;
        Product newProduct = new Product();
        newProduct.setId(newProductId);
        newProduct.setName("New Product");
        newProduct.setCategory(category);

        SKURequest updateRequest = SKURequest.builder()
            .productId(newProductId)
            .color("Blue")
            .size("L")
            .price(new BigDecimal("39.99"))
            .stockQuantity(50)
            .build();

        when(skuRepository.findByIdWithProductAndCategory(skuId)).thenReturn(sku);
        when(productRepository.findById(newProductId)).thenReturn(Optional.of(newProduct));
        when(skuRepository.existsByProductIdAndColorAndSize(
            newProductId, updateRequest.getColor(), updateRequest.getSize()))
            .thenReturn(false);
        when(skuRepository.save(any(SKU.class))).thenReturn(sku);

        // Act
        SKUResponse response = skuService.updateSKU(skuId, updateRequest);

        // Assert
        assertNotNull(response);
        verify(productRepository).findById(newProductId);
        verify(skuRepository).existsByProductIdAndColorAndSize(
            newProductId, updateRequest.getColor(), updateRequest.getSize());
        verify(skuRepository).save(any(SKU.class));
    }

    @Test
    @DisplayName("Should throw exception when updating SKU with duplicate color and size in new product")
    void updateSKU_WithDuplicateInNewProduct_ShouldThrowException() {
        // Arrange
        Long skuId = 1L;
        Long newProductId = 2L;
        Product newProduct = new Product();
        newProduct.setId(newProductId);
        newProduct.setName("New Product");
        newProduct.setCategory(category);

        SKURequest updateRequest = SKURequest.builder()
            .productId(newProductId)
            .color("Blue")
            .size("L")
            .price(new BigDecimal("39.99"))
            .stockQuantity(50)
            .build();

        when(skuRepository.findByIdWithProductAndCategory(skuId)).thenReturn(sku);
        when(productRepository.findById(newProductId)).thenReturn(Optional.of(newProduct));
        when(skuRepository.existsByProductIdAndColorAndSize(
            newProductId, updateRequest.getColor(), updateRequest.getSize()))
            .thenReturn(true);

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
            DataIntegrityViolationException.class,
            () -> skuService.updateSKU(skuId, updateRequest)
        );

        assertEquals(
            String.format("SKU with color '%s' and size '%s' already exists for the target product",
                updateRequest.getColor(), updateRequest.getSize()),
            exception.getMessage()
        );
        verify(productRepository).findById(newProductId);
        verify(skuRepository).existsByProductIdAndColorAndSize(
            newProductId, updateRequest.getColor(), updateRequest.getSize());
        verify(skuRepository, never()).save(any(SKU.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent SKU")
    void updateSKU_WithNonExistentId_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        when(skuRepository.findByIdWithProductAndCategory(nonExistentId))
            .thenReturn(null);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> skuService.updateSKU(nonExistentId, validRequest)
        );

        assertEquals("SKU not found with id: 999", exception.getMessage());
        verify(skuRepository).findByIdWithProductAndCategory(nonExistentId);
        verify(skuRepository, never()).save(any(SKU.class));
    }

    @Test
    @DisplayName("Should successfully get SKU by ID")
    void getSKUById_WithValidId_ShouldReturnSKU() {
        // Arrange
        Long skuId = 1L;
        when(skuRepository.findByIdWithProductAndCategory(skuId))
            .thenReturn(sku);

        // Act
        SKUResponse response = skuService.getSKUById(skuId);

        // Assert
        assertNotNull(response);
        assertEquals(sku.getId(), response.getId());
        assertEquals(sku.getProduct().getId(), response.getProductId());
        assertEquals(sku.getProduct().getName(), response.getProductName());
        assertEquals(sku.getColor(), response.getColor());
        assertEquals(sku.getSize(), response.getSize());
        assertEquals(sku.getPrice(), response.getPrice());
        assertEquals(sku.getStockQuantity(), response.getStockQuantity());

        verify(skuRepository).findByIdWithProductAndCategory(skuId);
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent SKU")
    void getSKUById_WithNonExistentId_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        when(skuRepository.findByIdWithProductAndCategory(nonExistentId))
            .thenReturn(null);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> skuService.getSKUById(nonExistentId)
        );

        assertEquals("SKU not found with id: 999", exception.getMessage());
        verify(skuRepository).findByIdWithProductAndCategory(nonExistentId);
    }

    @Test
    @DisplayName("Should successfully get SKUs by product ID")
    void getSKUsByProductId_WithValidProductId_ShouldReturnSKUs() {
        // Arrange
        Long productId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<SKU> skuPage = new PageImpl<>(List.of(sku));

        when(productRepository.existsById(productId)).thenReturn(true);
        when(skuRepository.findByProductId(productId, pageable)).thenReturn(skuPage);

        // Act
        Page<SKUResponse> response = skuService.getSKUsByProductId(productId, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        SKUResponse skuResponse = response.getContent().get(0);
        assertEquals(sku.getId(), skuResponse.getId());
        assertEquals(sku.getProduct().getId(), skuResponse.getProductId());

        verify(productRepository).existsById(productId);
        verify(skuRepository).findByProductId(productId, pageable);
    }

    @Test
    @DisplayName("Should throw exception when getting SKUs for non-existent product")
    void getSKUsByProductId_WithNonExistentProductId_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> skuService.getSKUsByProductId(nonExistentId, pageable)
        );

        assertEquals("Product not found with id: 999", exception.getMessage());
        verify(productRepository).existsById(nonExistentId);
        verify(skuRepository, never()).findByProductId(any(), any());
    }

    @Test
    @DisplayName("Should successfully delete SKU")
    void deleteSKU_WithValidId_ShouldDeleteSKU() {
        // Arrange
        Long skuId = 1L;
        when(skuRepository.existsById(skuId)).thenReturn(true);
        doNothing().when(skuRepository).deleteById(skuId);

        // Act
        skuService.deleteSKU(skuId);

        // Assert
        verify(skuRepository).existsById(skuId);
        verify(skuRepository).deleteById(skuId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent SKU")
    void deleteSKU_WithNonExistentId_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        when(skuRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> skuService.deleteSKU(nonExistentId)
        );

        assertEquals("SKU not found with id: 999", exception.getMessage());
        verify(skuRepository).existsById(nonExistentId);
        verify(skuRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should successfully get SKU by ID with all fields mapped correctly")
    void getSKUById_ShouldMapAllFieldsCorrectly() {
        // Arrange
        Long skuId = 1L;
        SKU sku = new SKU();
        sku.setId(skuId);
        sku.setColor("Red");
        sku.setSize("M");
        sku.setPrice(new BigDecimal("29.99"));
        sku.setStockQuantity(100);
        
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        
        product.setCategory(category);
        sku.setProduct(product);

        when(skuRepository.findByIdWithProductAndCategory(skuId)).thenReturn(sku);

        // Act
        SKUResponse response = skuService.getSKUById(skuId);

        // Assert
        assertNotNull(response);
        assertEquals(sku.getId(), response.getId());
        assertEquals(sku.getProduct().getId(), response.getProductId());
        assertEquals(sku.getProduct().getName(), response.getProductName());
        assertEquals(sku.getColor(), response.getColor());
        assertEquals(sku.getSize(), response.getSize());
        assertEquals(sku.getPrice(), response.getPrice());
        assertEquals(sku.getStockQuantity(), response.getStockQuantity());
        assertEquals(sku.getProduct().getCategory().getName(), response.getCategoryName());

        verify(skuRepository).findByIdWithProductAndCategory(skuId);
    }

    @Test
    @DisplayName("Should successfully get SKUs by product ID with complete mapping")
    void getSKUsByProductId_WithMapping_ShouldReturnMappedSKUs() {
        // Arrange
        Long productId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        // Create first SKU
        SKU sku1 = new SKU();
        sku1.setId(1L);
        sku1.setColor("Red");
        sku1.setSize("M");
        sku1.setPrice(new BigDecimal("29.99"));
        sku1.setStockQuantity(100);
        sku1.setProduct(product);

        // Create second SKU with different values
        SKU sku2 = new SKU();
        sku2.setId(2L);
        sku2.setColor("Blue");
        sku2.setSize("L");
        sku2.setPrice(new BigDecimal("39.99"));
        sku2.setStockQuantity(50);
        sku2.setProduct(product);

        List<SKU> skus = List.of(sku1, sku2);
        Page<SKU> skuPage = new PageImpl<>(skus);

        when(productRepository.existsById(productId)).thenReturn(true);
        when(skuRepository.findByProductId(productId, pageable)).thenReturn(skuPage);

        // Act
        Page<SKUResponse> response = skuService.getSKUsByProductId(productId, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getTotalElements());

        List<SKUResponse> content = response.getContent();

        // Verify first SKU mapping
        SKUResponse response1 = content.get(0);
        assertEquals(sku1.getId(), response1.getId());
        assertEquals(sku1.getProduct().getId(), response1.getProductId());
        assertEquals(sku1.getProduct().getName(), response1.getProductName());
        assertEquals(sku1.getColor(), response1.getColor());
        assertEquals(sku1.getSize(), response1.getSize());
        assertEquals(sku1.getPrice(), response1.getPrice());
        assertEquals(sku1.getStockQuantity(), response1.getStockQuantity());
        assertEquals(sku1.getProduct().getCategory().getName(), response1.getCategoryName());

        // Verify second SKU mapping
        SKUResponse response2 = content.get(1);
        assertEquals(sku2.getId(), response2.getId());
        assertEquals(sku2.getProduct().getId(), response2.getProductId());
        assertEquals(sku2.getProduct().getName(), response2.getProductName());
        assertEquals(sku2.getColor(), response2.getColor());
        assertEquals(sku2.getSize(), response2.getSize());
        assertEquals(sku2.getPrice(), response2.getPrice());
        assertEquals(sku2.getStockQuantity(), response2.getStockQuantity());
        assertEquals(sku2.getProduct().getCategory().getName(), response2.getCategoryName());

        verify(productRepository).existsById(productId);
        verify(skuRepository).findByProductId(productId, pageable);
    }
} 
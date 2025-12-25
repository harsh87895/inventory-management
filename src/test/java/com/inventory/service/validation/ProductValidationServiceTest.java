package com.inventory.service.validation;

import com.inventory.dto.request.ProductRequest;
import com.inventory.exception.ProductException;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.model.Category;
import com.inventory.model.Product;
import com.inventory.model.SKU;
import com.inventory.repository.CategoryRepository;
import com.inventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductValidationServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductValidationService validationService;

    private Category activeCategory;
    private Category inactiveCategory;
    private Product existingProduct;
    private ProductRequest validRequest;

    @BeforeEach
    void setUp() {
        // Set up active category
        activeCategory = new Category();
        activeCategory.setId(1L);
        activeCategory.setName("Active Category");
        activeCategory.setActive(true);

        // Set up inactive category
        inactiveCategory = new Category();
        inactiveCategory.setId(2L);
        inactiveCategory.setName("Inactive Category");
        inactiveCategory.setActive(false);

        // Set up existing product
        existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Existing Product");
        existingProduct.setCategory(activeCategory);
        existingProduct.setSkus(new ArrayList<>());

        // Set up valid request
        validRequest = ProductRequest.builder()
                .name("Test Product")
                .description("A valid product description")
                .categoryId(1L)
                .build();
    }

    @Test
    void validateProductCreation_WithValidRequest_ShouldNotThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);

        assertDoesNotThrow(() -> validationService.validateProductCreation(validRequest));
    }

    @Test
    void validateProductCreation_WithNonexistentCategory_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> validationService.validateProductCreation(validRequest)
        );
        assertEquals("Category not found with id: 1", exception.getMessage());
    }

    @Test
    void validateProductCreation_WithInactiveCategory_ShouldThrowException() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(inactiveCategory));
        validRequest.setCategoryId(2L);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> validationService.validateProductCreation(validRequest)
        );
        assertEquals("Cannot create product. Category ID: 2 is not active", exception.getMessage());
    }

    @Test
    void validateProductCreation_WithDuplicateName_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId("Test Product", 1L)).thenReturn(true);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> validationService.validateProductCreation(validRequest)
        );
        assertEquals("Product with name 'Test Product' already exists in category ID: 1", exception.getMessage());
    }

    @Test
    void validateProductUpdate_WithValidRequest_ShouldNotThrowException() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);

        assertDoesNotThrow(() -> validationService.validateProductUpdate(1L, validRequest));
    }

    @Test
    void validateProductUpdate_WithNonexistentProduct_ShouldThrowException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> validationService.validateProductUpdate(1L, validRequest)
        );
        assertEquals("Product not found with id: 1", exception.getMessage());
    }

    @Test
    void validateProductUpdate_WithInactiveCategory_ShouldThrowException() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(inactiveCategory));
        validRequest.setCategoryId(2L);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> validationService.validateProductUpdate(1L, validRequest)
        );
        assertEquals("Cannot create product. Category ID: 2 is not active", exception.getMessage());
    }

    @Test
    void validateProductUpdate_WithCategoryChangeAndExistingSKUs_ShouldThrowException() {
        List<SKU> skus = new ArrayList<>();
        skus.add(new SKU());
        existingProduct.setSkus(skus);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(activeCategory));
        validRequest.setCategoryId(2L);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> validationService.validateProductUpdate(1L, validRequest)
        );
        assertTrue(exception.getMessage().contains("Cannot change category for product ID: 1"));
    }

    @Test
    void validateProductUpdate_WithDuplicateName_ShouldThrowException() {
        Product differentProduct = new Product();
        differentProduct.setId(2L);
        differentProduct.setName("Different Name");
        differentProduct.setCategory(activeCategory);

        when(productRepository.findById(2L)).thenReturn(Optional.of(differentProduct));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId("Test Product", 1L)).thenReturn(true);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> validationService.validateProductUpdate(2L, validRequest)
        );
        assertEquals("Product with name 'Test Product' already exists in category ID: 1", exception.getMessage());
    }

    @Test
    void validateProductDeletion_WithValidRequest_ShouldNotThrowException() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        assertDoesNotThrow(() -> validationService.validateProductDeletion(1L));
    }

    @Test
    void validateProductDeletion_WithNonexistentProduct_ShouldThrowException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> validationService.validateProductDeletion(1L)
        );
        assertEquals("Product not found with id: 1", exception.getMessage());
    }

    @Test
    void validateProductDeletion_WithExistingSKUs_ShouldThrowException() {
        List<SKU> skus = new ArrayList<>();
        skus.add(new SKU());
        existingProduct.setSkus(skus);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        ProductException exception = assertThrows(
                ProductException.class,
                () -> validationService.validateProductDeletion(1L)
        );
        assertEquals("Cannot delete product ID: 1. It has active SKUs", exception.getMessage());
    }

    @Test
    void validateDescription_WithValidDescription_ShouldNotThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        assertDoesNotThrow(() -> validationService.validateProductCreation(validRequest));
    }

    @Test
    void validateDescription_WithHTMLTags_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        validRequest.setDescription("Description with <script>alert('xss')</script>");

        ProductException exception = assertThrows(
                ProductException.class,
                () -> validationService.validateProductCreation(validRequest)
        );
        assertEquals("Invalid product description format: HTML tags and potentially harmful content are not allowed", exception.getMessage());
    }

    @Test
    void validateDescription_WithSQLInjection_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        validRequest.setDescription("Description with SELECT * FROM users");

        ProductException exception = assertThrows(
                ProductException.class,
                () -> validationService.validateProductCreation(validRequest)
        );
        assertEquals("Invalid product description format: Invalid characters or SQL keywords detected", exception.getMessage());
    }

    @Test
    void validateDescription_WithTooFewWords_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        validRequest.setDescription("Too short");

        ProductException exception = assertThrows(
                ProductException.class,
                () -> validationService.validateProductCreation(validRequest)
        );
        assertEquals("Invalid product description format: Description must contain at least 3 words", exception.getMessage());
    }

    @Test
    void validateDescription_WithRequiredDescriptionMissing_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        validRequest.setName("Premium Set");
        validRequest.setDescription(null);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> validationService.validateProductCreation(validRequest)
        );
        assertEquals("Invalid product description format: Description is required for products with type: set, kit, bundle, or collection", 
                exception.getMessage());
    }

    @Test
    void validateDescription_WithMaxLengthExceeded_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        
        // Create a string longer than 1000 characters
        StringBuilder longDesc = new StringBuilder();
        for (int i = 0; i < 1001; i++) {
            longDesc.append("a");
        }
        validRequest.setDescription(longDesc.toString());

        ProductException exception = assertThrows(
            ProductException.class,
            () -> validationService.validateProductCreation(validRequest)
        );
        assertEquals("Invalid product description format: Description cannot exceed 1000 characters", 
            exception.getMessage());
    }

    @Test
    void validateDescription_WithOnlyWhitespace_ShouldNotThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        validRequest.setDescription("   \t\n   ");
        validRequest.setName("Regular Product"); // Not a special type

        assertDoesNotThrow(() -> validationService.validateProductCreation(validRequest));
    }

    @Test
    void validateDescription_WithOnlyWhitespaceForSpecialType_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        validRequest.setDescription("   \t\n   ");
        validRequest.setName("Premium Set"); // Special type

        ProductException exception = assertThrows(
            ProductException.class,
            () -> validationService.validateProductCreation(validRequest)
        );
        assertEquals("Invalid product description format: Description is required for products with type: set, kit, bundle, or collection",
            exception.getMessage());
    }

    @Test
    void validateDescription_WithAdditionalSQLKeywords_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        validRequest.setDescription("Description with DROP TABLE users");

        ProductException exception = assertThrows(
            ProductException.class,
            () -> validationService.validateProductCreation(validRequest)
        );
        assertEquals("Invalid product description format: Invalid characters or SQL keywords detected",
            exception.getMessage());
    }

    @Test
    void validateDescription_WithJavaScriptURL_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        validRequest.setDescription("Click here javascript:alert(1)");

        ProductException exception = assertThrows(
            ProductException.class,
            () -> validationService.validateProductCreation(validRequest)
        );
        assertEquals("Invalid product description format: HTML tags and potentially harmful content are not allowed",
            exception.getMessage());
    }

    @Test
    void validateDescription_WithDataURL_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        validRequest.setDescription("Image: data:image/jpeg;base64,/9j/4AAQ");

        ProductException exception = assertThrows(
            ProductException.class,
            () -> validationService.validateProductCreation(validRequest)
        );
        assertEquals("Invalid product description format: HTML tags and potentially harmful content are not allowed",
            exception.getMessage());
    }

    @Test
    void validateDescription_WithRepeatedCharacters_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        validRequest.setDescription("This is a spammmmmmy description");

        ProductException exception = assertThrows(
            ProductException.class,
            () -> validationService.validateProductCreation(validRequest)
        );
        assertEquals("Invalid product description format: Description contains excessive repeated characters",
            exception.getMessage());
    }

    @Test
    void validateDescription_WithNullDescription_NonSpecialType_ShouldNotThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        validRequest.setDescription(null);
        validRequest.setName("Regular Product");

        assertDoesNotThrow(() -> validationService.validateProductCreation(validRequest));
    }

    @Test
    void validateDescription_WithShortTextButNotSpecialType_ShouldNotThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(productRepository.existsByNameAndCategoryId(any(), anyLong())).thenReturn(false);
        validRequest.setName("Regular Product");
        validRequest.setDescription("");

        assertDoesNotThrow(() -> validationService.validateProductCreation(validRequest));
    }
} 
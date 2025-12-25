package com.inventory.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductNameValidatorTest {

    private ProductNameValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new ProductNameValidator();
        validator.initialize(null);
    }

    @Test
    void whenNameIsNull_thenValid() {
        assertTrue(validator.isValid(null, context), "Null values should be valid as @NotBlank handles null validation");
    }

    @Test
    void whenNameIsEmpty_thenInvalid() {
        assertFalse(validator.isValid("", context));
    }

    @Test
    void whenNameStartsWithNumber_thenInvalid() {
        assertFalse(validator.isValid("1Product", context));
    }

    @Test
    void whenNameContainsInvalidCharacters_thenInvalid() {
        assertFalse(validator.isValid("Product@Name", context));
        assertFalse(validator.isValid("Product#123", context));
        assertFalse(validator.isValid("Product$Name", context));
    }

    @Test
    void whenNameIsValid_thenValid() {
        assertTrue(validator.isValid("Product", context));
        assertTrue(validator.isValid("Product 123", context));
        assertTrue(validator.isValid("Product-Name", context));
        assertTrue(validator.isValid("Product (Large)", context));
    }
} 
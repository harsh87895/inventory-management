package com.inventory.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PriceValidatorTest {

    private PriceValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new PriceValidator();
        validator.initialize(null);
    }

    @Test
    void whenPriceIsNull_thenValid() {
        assertTrue(validator.isValid(null, context), "Null values should be valid as @NotNull handles null validation");
    }

    @Test
    void whenPriceIsNegative_thenInvalid() {
        assertFalse(validator.isValid(BigDecimal.valueOf(-10.00), context));
        assertFalse(validator.isValid(BigDecimal.valueOf(-0.01), context));
    }

    @Test
    void whenPriceIsZero_thenInvalid() {
        assertFalse(validator.isValid(BigDecimal.ZERO, context));
    }

    @Test
    void whenPriceHasMoreThanTwoDecimals_thenInvalid() {
        assertFalse(validator.isValid(BigDecimal.valueOf(10.999), context));
        assertFalse(validator.isValid(BigDecimal.valueOf(99.001), context));
    }

    @Test
    void whenPriceIsValid_thenValid() {
        assertTrue(validator.isValid(BigDecimal.valueOf(10.00), context));
        assertTrue(validator.isValid(BigDecimal.valueOf(99.99), context));
        assertTrue(validator.isValid(BigDecimal.valueOf(100), context));
        assertTrue(validator.isValid(BigDecimal.ONE, context));
    }
} 
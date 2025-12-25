package com.inventory.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class PriceValidator implements ConstraintValidator<ValidPrice, BigDecimal> {

    @Override
    public void initialize(ValidPrice constraintAnnotation) {
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull handle null validation
        }
        
        // Check if price is positive and has at most 2 decimal places
        return value.compareTo(BigDecimal.ZERO) > 0 && 
               value.scale() <= 2;
    }
} 
package com.inventory.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductNameValidator implements ConstraintValidator<ValidProductName, String> {

    @Override
    public void initialize(ValidProductName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotBlank handle null validation
        }

        // Product name must:
        // 1. Start with a letter
        // 2. Contain only letters, numbers, spaces, hyphens, and parentheses
        // 3. Not have consecutive spaces or special characters
        // 4. Not end with a space or special character
        return value.matches("^[A-Za-z][A-Za-z0-9\\s\\-()]*[A-Za-z0-9)]$") &&
               !value.contains("  ") && // No consecutive spaces
               !value.contains("--") && // No consecutive hyphens
               !value.contains("()") && // No empty parentheses
               !value.contains(")("); // No consecutive parentheses
    }
} 
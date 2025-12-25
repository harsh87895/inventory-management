package com.inventory.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProductNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductName {
    String message() default "Invalid product name format. Name must start with a letter and can contain letters, numbers, spaces, hyphens, and parentheses";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 
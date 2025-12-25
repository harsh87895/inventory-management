package com.inventory.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PriceValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrice {
    String message() default "Invalid price format. Price must be greater than 0 and have at most 2 decimal places";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 
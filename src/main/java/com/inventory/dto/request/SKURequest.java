package com.inventory.dto.request;

import com.inventory.validation.ValidPrice;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SKURequest {
    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Long productId;

    @NotBlank(message = "Color is required")
    @Pattern(regexp = "^[a-zA-Z\\s-]{2,50}$", 
            message = "Color must be 2-50 characters long and contain only letters, spaces, and hyphens")
    private String color;

    @NotBlank(message = "Size is required")
    @Pattern(regexp = "^(XS|S|M|L|XL|XXL|\\d{1,3}(cm)?|\\d{1,2}\\.\\d{1}(cm)?)$",
            message = "Size must be a valid format (XS, S, M, L, XL, XXL, or a number followed by optional 'cm')")
    private String size;

    @NotNull(message = "Price is required")
    @ValidPrice
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    @Max(value = 999999, message = "Stock quantity cannot exceed 999,999")
    private Integer stockQuantity;
} 
package com.inventory.dto.request;

import com.inventory.validation.ValidProductName;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    @ValidProductName
    private String name;

    @Pattern(regexp = "^$|^[A-Za-z0-9\\s\\-.,!?()]{10,500}$",
            message = "Description must be between 10 and 500 characters and can only contain letters, numbers, spaces, and basic punctuation")
    private String description;

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Long categoryId;

    // Custom validation to ensure description is provided for certain product names
    public boolean requiresDescription() {
        if (name == null) return false;
        // If product name contains certain keywords, description should be mandatory
        String lowercaseName = name.toLowerCase();
        return lowercaseName.contains("set") ||
               lowercaseName.contains("kit") ||
               lowercaseName.contains("bundle") ||
               lowercaseName.contains("collection");
    }
} 
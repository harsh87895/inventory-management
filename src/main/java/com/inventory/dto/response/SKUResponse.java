package com.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SKUResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String color;
    private String size;
    private BigDecimal price;
    private Integer stockQuantity;
    private String categoryName;
} 
package com.inventory.mapper;

import com.inventory.dto.request.ProductRequest;
import com.inventory.dto.response.ProductResponse;
import com.inventory.model.Category;
import com.inventory.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request, Category category) {
        Product product = new Product();
        updateEntity(product, request, category);
        return product;
    }

    public void updateEntity(Product product, ProductRequest request, Category category) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        if (product.getSkus() == null) {
            product.setSkus(new ArrayList<>());
        }
    }

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .skuCount(product.getSkus() != null ? product.getSkus().size() : 0)
                .build();
    }
} 
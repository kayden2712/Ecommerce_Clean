package com.example.ecommerce_clean.modules.product.application.dto;

public record ProductResponse(
        Long id,
        String name,
        Double price,
        Integer stock,
        String color,
        String categoryName
) {
}

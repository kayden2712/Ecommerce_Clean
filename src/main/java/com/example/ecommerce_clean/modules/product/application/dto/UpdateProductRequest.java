package com.example.ecommerce_clean.modules.product.application.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;

public record UpdateProductRequest(
        String name,

        @DecimalMin("0.0")
        BigDecimal price,

        Integer stock,

        String color,

        Long categoryId
) {
}

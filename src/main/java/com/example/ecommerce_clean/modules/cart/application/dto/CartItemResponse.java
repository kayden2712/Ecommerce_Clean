package com.example.ecommerce_clean.modules.cart.application.dto;

public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        Double price,
        Integer quantity,
        String selectedColor,
        String availableColor
) {
}

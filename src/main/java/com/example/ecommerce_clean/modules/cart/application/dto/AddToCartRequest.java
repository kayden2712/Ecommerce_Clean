package com.example.ecommerce_clean.modules.cart.application.dto;

public record AddToCartRequest(Long productId, Integer quantity, String selectedColor) {
}

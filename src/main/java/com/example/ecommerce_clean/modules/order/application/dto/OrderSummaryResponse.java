package com.example.ecommerce_clean.modules.order.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.ecommerce_clean.shared.enums.OrderStatus;

/**
 * Lightweight order summary for listing views
 * Use this for order history, search results, etc.
 * Does not include item details to reduce payload size
 */
public record OrderSummaryResponse(
        Long id,
        Long userId,
        String username,
        BigDecimal totalPrice,
        OrderStatus status,
        int itemCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

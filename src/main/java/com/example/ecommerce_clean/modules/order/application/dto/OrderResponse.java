package com.example.ecommerce_clean.modules.order.application.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.ecommerce_clean.shared.enums.OrderStatus;

public record OrderResponse(
        Long id,
        BigDecimal totalPrice,
        OrderStatus status,
        List<OrderItemResponse> items
) {
}

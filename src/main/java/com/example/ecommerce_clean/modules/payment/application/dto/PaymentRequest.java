package com.example.ecommerce_clean.modules.payment.application.dto;

import java.math.BigDecimal;

public record PaymentRequest(
        Long orderId,
        BigDecimal amount,
        String paymentMethod
) {
}

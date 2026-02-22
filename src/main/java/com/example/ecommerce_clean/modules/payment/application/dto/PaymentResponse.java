package com.example.ecommerce_clean.modules.payment.application.dto;

import java.math.BigDecimal;

import com.example.ecommerce_clean.shared.enums.PaymentMethod;
import com.example.ecommerce_clean.shared.enums.PaymentStatus;

public record PaymentResponse(
        Long id,
        Long orderId,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        PaymentStatus status,
        String transactionId
) {
}

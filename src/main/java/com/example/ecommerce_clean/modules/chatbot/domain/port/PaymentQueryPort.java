package com.example.ecommerce_clean.modules.chatbot.domain.port;

public interface PaymentQueryPort {
    String getPaymentStatus(Long userId, String message);
}

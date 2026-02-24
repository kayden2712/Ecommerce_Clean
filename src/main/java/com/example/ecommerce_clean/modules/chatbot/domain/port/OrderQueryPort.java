package com.example.ecommerce_clean.modules.chatbot.domain.port;

public interface OrderQueryPort {
    String getOrderInfo(Long userId, String message);
}

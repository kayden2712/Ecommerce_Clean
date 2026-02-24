package com.example.ecommerce_clean.modules.chatbot.application.intent;

import com.example.ecommerce_clean.shared.enums.IntentType;

/**
 * Simple keyword-based intent detection.
 * Supports both English and Vietnamese keywords.
 */
public final class IntentDetector {

    private IntentDetector() {}

    public static IntentType detect(String userMessage) {
        String lower = userMessage.toLowerCase();

        if (matchesAny(lower, "order", "đơn hàng", "đơn", "đặt hàng", "giao hàng", "tracking")) {
            return IntentType.ORDER_QUERY;
        }
        if (matchesAny(lower, "product", "sản phẩm", "hàng", "mặt hàng", "giá", "price", "tìm", "search")) {
            return IntentType.PRODUCT_QUERY;
        }
        if (matchesAny(lower, "payment", "thanh toán", "pay", "trả tiền", "hoá đơn", "hóa đơn")) {
            return IntentType.PAYMENT_QUERY;
        }
        return IntentType.GENERAL_CHAT;
    }

    private static boolean matchesAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}

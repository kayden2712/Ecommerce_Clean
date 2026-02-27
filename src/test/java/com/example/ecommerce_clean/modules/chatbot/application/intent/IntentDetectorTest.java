package com.example.ecommerce_clean.modules.chatbot.application.intent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.ecommerce_clean.shared.enums.IntentType;

class IntentDetectorTest {

    @ParameterizedTest
    @ValueSource(strings = {"đơn hàng của tôi", "kiểm tra đơn", "order status", "tracking đơn", "giao hàng"})
    @DisplayName("Detect ORDER_QUERY từ các keyword liên quan đơn hàng")
    void shouldDetectOrderQuery(String message) {
        assertEquals(IntentType.ORDER_QUERY, IntentDetector.detect(message));
    }

    @ParameterizedTest
    @ValueSource(strings = {"tìm sản phẩm", "giá bao nhiêu", "product list", "search laptop", "mặt hàng"})
    @DisplayName("Detect PRODUCT_QUERY từ các keyword liên quan sản phẩm")
    void shouldDetectProductQuery(String message) {
        assertEquals(IntentType.PRODUCT_QUERY, IntentDetector.detect(message));
    }

    @ParameterizedTest
    @ValueSource(strings = {"thanh toán đơn", "payment status", "trả tiền", "hoá đơn", "hóa đơn"})
    @DisplayName("Detect PAYMENT_QUERY từ các keyword liên quan thanh toán")
    void shouldDetectPaymentQuery(String message) {
        assertEquals(IntentType.PAYMENT_QUERY, IntentDetector.detect(message));
    }

    @ParameterizedTest
    @ValueSource(strings = {"xin chào", "hello", "bạn khỏe không?", "cảm ơn", "tạm biệt"})
    @DisplayName("Detect GENERAL_CHAT khi không match keyword nào")
    void shouldDetectGeneralChat(String message) {
        assertEquals(IntentType.GENERAL_CHAT, IntentDetector.detect(message));
    }

    @Test
    @DisplayName("Detect case-insensitive: 'ORDER' → ORDER_QUERY")
    void shouldDetectCaseInsensitive() {
        assertEquals(IntentType.ORDER_QUERY, IntentDetector.detect("CHECK MY ORDER"));
    }
}

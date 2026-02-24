package com.example.ecommerce_clean.modules.chatbot.infrastructure.payment;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.chatbot.domain.port.PaymentQueryPort;
import com.example.ecommerce_clean.modules.payment.application.dto.PaymentResponse;
import com.example.ecommerce_clean.modules.payment.application.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentQueryAdapter implements PaymentQueryPort {

    private final PaymentService paymentService;

    @Override
    public String getPaymentStatus(Long userId, String message) {
        try {
            Long orderId = extractOrderId(message);
            if (orderId == null) {
                return "Vui lòng cung cấp mã đơn hàng để tôi tra cứu trạng thái thanh toán (VD: 'thanh toán đơn 123').";
            }

            PaymentResponse payment = paymentService.getPaymentByOrderId(orderId);
            return String.format(
                    "Thanh toán đơn hàng #%d | Trạng thái: %s | Số tiền: %,.0f VND | Phương thức: %s | Mã giao dịch: %s",
                    payment.orderId(), payment.status(), payment.amount(),
                    payment.paymentMethod(), payment.transactionId()
            );

        } catch (Exception e) {
            log.error("Error fetching payment status for userId={}", userId, e);
            return "Không thể lấy thông tin thanh toán lúc này.";
        }
    }

    private Long extractOrderId(String message) {
        var matcher = Pattern.compile("\\d+").matcher(message);
        if (matcher.find()) {
            try {
                return Long.parseLong(matcher.group());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}

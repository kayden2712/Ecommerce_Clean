package com.example.ecommerce_clean.modules.chatbot.infrastructure.order;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.chatbot.domain.port.OrderQueryPort;
import com.example.ecommerce_clean.modules.order.application.dto.OrderResponse;
import com.example.ecommerce_clean.modules.order.application.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderQueryAdapter implements OrderQueryPort {

    private final OrderService orderService;

    @Override
    public String getOrderInfo(Long userId, String message) {
        try {
            // Try to extract a specific order ID from the message
            Long orderId = extractOrderId(message);
            if (orderId != null) {
                OrderResponse order = orderService.getOrder(orderId, null);
                return formatOrder(order);
            }

            // Otherwise return recent order history (username-based lookup not available here,
            // so we return a generic message if we can't find by ID)
            return "Vui lòng cung cấp mã đơn hàng (VD: 'đơn hàng 123') để tôi tra cứu.";

        } catch (Exception e) {
            log.error("Error fetching order info for userId={}", userId, e);
            return "Không thể lấy thông tin đơn hàng lúc này.";
        }
    }

    private String formatOrder(OrderResponse order) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Đơn hàng #%d | Trạng thái: %s | Tổng tiền: %,.0f VND\n",
                order.id(), order.status(), order.totalPrice()));
        if (order.items() != null) {
            sb.append("Chi tiết:\n");
            order.items().forEach(item -> sb.append(String.format("  - %s x%d: %,.0f VND\n",
                    item.productName(), item.quantity(), item.totalPrice())));
        }
        return sb.toString();
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

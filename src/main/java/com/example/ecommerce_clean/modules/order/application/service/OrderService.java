package com.example.ecommerce_clean.modules.order.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ecommerce_clean.modules.order.application.dto.OrderResponse;
import com.example.ecommerce_clean.shared.enums.OrderStatus;

public interface OrderService {

    OrderResponse placeOrder(String username);

    void cancelOrder(Long orderId, String username);

    OrderResponse getOrder(Long orderId, String username);

    Page<OrderResponse> getOrderHistory(String username, OrderStatus status, Pageable pageable);
}

package com.example.ecommerce_clean.modules.order.domain.repository;

import java.util.List;

import com.example.ecommerce_clean.modules.order.domain.entity.OrderItem;

public interface OrderItemRepository {

    List<OrderItem> findByOrderId(Long orderId);

    OrderItem save(OrderItem orderItem);
}

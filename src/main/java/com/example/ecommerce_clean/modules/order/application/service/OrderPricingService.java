package com.example.ecommerce_clean.modules.order.application.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.ecommerce_clean.modules.order.domain.entity.OrderItem;

public interface OrderPricingService {

    BigDecimal calculateItemTotal(OrderItem orderItem);

    BigDecimal calculateOrderTotal(List<OrderItem> orderItems);
}

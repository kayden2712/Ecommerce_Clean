package com.example.ecommerce_clean.modules.order.application.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ecommerce_clean.modules.order.domain.entity.OrderItem;
@Service
public class OrderPricingServiceImpl implements OrderPricingService {

    @Override
    public BigDecimal calculateItemTotal(OrderItem orderItem) {
        return orderItem.getPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
    }

    @Override
    public BigDecimal calculateOrderTotal(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::calculateItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

package com.example.ecommerce_clean.modules.order.application.service;

import com.example.ecommerce_clean.modules.order.domain.entity.Order;

public interface OrderValidator {

    void validateCancel(Order order);
}

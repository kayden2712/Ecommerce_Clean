package com.example.ecommerce_clean.modules.order.application.service;

import com.example.ecommerce_clean.modules.order.domain.entity.Order;

//Chuyen trang thai don hang
public interface OrderValidator {

    void validateCancel(Order order);
}

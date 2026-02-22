package com.example.ecommerce_clean.modules.order.application.service;

import org.springframework.stereotype.Service;

import com.example.ecommerce_clean.common.exception.domain.InvalidOperationException;
import com.example.ecommerce_clean.modules.order.domain.entity.Order;
import com.example.ecommerce_clean.shared.enums.ErrorCode;
import com.example.ecommerce_clean.shared.enums.OrderStatus;

@Service
public class OrderValidatorImpl implements OrderValidator {

    @Override
    public void validateCancel(Order order) {
        if (order.getStatus() == OrderStatus.PAID
                || order.getStatus() == OrderStatus.COMPLETED
                || order.getStatus() == OrderStatus.SHIPPED) {
            throw new InvalidOperationException(ErrorCode.ORDER_CANNOT_BE_CANCELLED);
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOperationException(ErrorCode.ORDER_ALREADY_CANCELLED);
        }
    }
}

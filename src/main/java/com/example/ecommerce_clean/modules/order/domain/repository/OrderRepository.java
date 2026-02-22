package com.example.ecommerce_clean.modules.order.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ecommerce_clean.modules.order.domain.entity.Order;
import com.example.ecommerce_clean.shared.enums.OrderStatus;

public interface OrderRepository {

    Optional<Order> findById(Long id);

    Order save(Order order);

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable);
}

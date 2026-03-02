package com.example.ecommerce_clean.modules.order.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.order.domain.entity.Order;
import com.example.ecommerce_clean.modules.order.domain.entity.OrderItem;
import com.example.ecommerce_clean.modules.order.infrastructure.persistence.entity.OrderItemJpaEntity;
import com.example.ecommerce_clean.modules.order.infrastructure.persistence.entity.OrderJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderPersistenceMapper {

    // Convert OrderJpaEntity to Order domain entity.
    public Order toDomain(OrderJpaEntity entity) {
        if (entity == null)
            return null;

        return Order.reconstitute(
                entity.getId(),
                entity.getUser().getId(),
                entity.getUser().getUsername(),
                entity.getStatus(),
                entity.getItems().stream().map(this::toItemDomain).toList(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    // Convert OrderItemJpaEntity to OrderItem domain entity.
    private OrderItem toItemDomain(OrderItemJpaEntity entity) {
        if (entity == null)
            return null;

        return OrderItem.reconstitute(
                entity.getId(),
                entity.getProduct().getId(),
                entity.getProduct().getName(),
                entity.getQuantity(),
                entity.getPrice(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}

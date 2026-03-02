package com.example.ecommerce_clean.modules.order.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.order.domain.entity.OrderItem;
import com.example.ecommerce_clean.modules.order.infrastructure.persistence.entity.OrderItemJpaEntity;

@Component
public class OrderItemPersistenceMapper {

    //  Convert OrderItemJpaEntity to OrderItem domain entity
    public OrderItem toDomain(OrderItemJpaEntity entity) {
        if (entity == null) return null;

        return OrderItem.reconstitute(
                entity.getId(),
                entity.getProduct().getId(),
                entity.getProduct().getName(),
                entity.getQuantity(),
                entity.getPrice(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

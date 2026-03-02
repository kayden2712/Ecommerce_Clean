package com.example.ecommerce_clean.modules.cart.infrastructure.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.cart.domain.entity.CartItem;
import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.entity.CartItemJpaEntity;

@Component
public class CartItemPersistenceMapper {

    // Convert CartItemJpaEntity to CartItem domain entity
    public CartItem toDomain(CartItemJpaEntity entity) {
        if (entity == null)
            return null;

        return CartItem.reconstitute(
                entity.getId(),
                entity.getProduct().getId(),
                entity.getProduct().getName(),
                entity.getProduct().getPrice(),
                entity.getProduct().getColor(),
                entity.getQuantity(),
                entity.getSelectedColor(),
                entity.getProduct().getStock(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    // Convert list of CartItemJpaEntity to list of CartItem domain entities
    public List<CartItem> toDomainList(List<CartItemJpaEntity> entities) {
        if (entities == null)
            return null;

        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}

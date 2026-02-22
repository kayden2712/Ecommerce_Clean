package com.example.ecommerce_clean.modules.cart.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ecommerce_clean.modules.cart.domain.entity.Cart;
import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.entity.CartJpaEntity;

@Mapper(componentModel = "spring")
public interface CartPersistenceMapper {

    @Mapping(source = "user.id", target = "userId")
    Cart toDomain(CartJpaEntity entity);

    CartJpaEntity toJpaEntity(Cart cart);
}

package com.example.ecommerce_clean.modules.order.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ecommerce_clean.modules.order.domain.entity.OrderItem;
import com.example.ecommerce_clean.modules.order.infrastructure.persistence.entity.OrderItemJpaEntity;

@Mapper(componentModel = "spring")
public interface OrderItemPersistenceMapper {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItem toDomain(OrderItemJpaEntity entity);
}

package com.example.ecommerce_clean.modules.order.infrastructure.persistence.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ecommerce_clean.modules.order.domain.entity.Order;
import com.example.ecommerce_clean.modules.order.infrastructure.persistence.entity.OrderJpaEntity;

@Mapper(componentModel = "spring", uses = OrderItemPersistenceMapper.class)
public interface OrderPersistenceMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    Order toDomain(OrderJpaEntity entity);

    List<Order> toDomainList(List<OrderJpaEntity> entities);
}

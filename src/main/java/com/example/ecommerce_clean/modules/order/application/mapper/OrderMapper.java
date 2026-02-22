package com.example.ecommerce_clean.modules.order.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ecommerce_clean.modules.order.application.dto.OrderItemResponse;
import com.example.ecommerce_clean.modules.order.application.dto.OrderResponse;
import com.example.ecommerce_clean.modules.order.domain.entity.Order;
import com.example.ecommerce_clean.modules.order.domain.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toResponse(Order order);

    @Mapping(source = "price", target = "unitPrice")
    OrderItemResponse toItemResponse(OrderItem orderItem);
}

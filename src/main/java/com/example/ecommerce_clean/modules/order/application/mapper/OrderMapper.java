package com.example.ecommerce_clean.modules.order.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ecommerce_clean.modules.order.application.dto.OrderItemResponse;
import com.example.ecommerce_clean.modules.order.application.dto.OrderResponse;
import com.example.ecommerce_clean.modules.order.application.dto.OrderSummaryResponse;
import com.example.ecommerce_clean.modules.order.domain.entity.Order;
import com.example.ecommerce_clean.modules.order.domain.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    //  Map full order details with all items
    OrderResponse toResponse(Order order);

    //  Map order summary for listing views (without item details)
    @Mapping(target = "itemCount", expression = "java(calculateItemCount(order))")
    OrderSummaryResponse toSummaryResponse(Order order);

  
    //  Map order item details
    @Mapping(source = "price", target = "unitPrice")
    OrderItemResponse toItemResponse(OrderItem orderItem);
    
  
    //  Calculate total item count across all order items
    default int calculateItemCount(Order order) {
        return order.getItems() != null 
            ? order.getItems().stream()
                .mapToInt(OrderItem::getQuantity)
                .sum() 
            : 0;
    }
}

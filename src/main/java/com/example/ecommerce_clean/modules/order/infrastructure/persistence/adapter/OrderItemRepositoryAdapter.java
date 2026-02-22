package com.example.ecommerce_clean.modules.order.infrastructure.persistence.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.order.domain.entity.OrderItem;
import com.example.ecommerce_clean.modules.order.domain.repository.OrderItemRepository;
import com.example.ecommerce_clean.modules.order.infrastructure.persistence.mapper.OrderItemPersistenceMapper;
import com.example.ecommerce_clean.modules.order.infrastructure.persistence.repository.OrderItemJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderItemRepositoryAdapter implements OrderItemRepository {

    private final OrderItemJpaRepository jpaRepository;
    private final OrderItemPersistenceMapper mapper;

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        return jpaRepository.findByOrderId(orderId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        // Order items are saved as part of the Order cascade
        throw new UnsupportedOperationException("Order items are saved via Order cascade");
    }
}

package com.example.ecommerce_clean.modules.order.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.order.domain.entity.Order;
import com.example.ecommerce_clean.modules.order.domain.entity.OrderItem;
import com.example.ecommerce_clean.modules.order.domain.repository.OrderRepository;
import com.example.ecommerce_clean.modules.order.infrastructure.persistence.entity.OrderItemJpaEntity;
import com.example.ecommerce_clean.modules.order.infrastructure.persistence.entity.OrderJpaEntity;
import com.example.ecommerce_clean.modules.order.infrastructure.persistence.mapper.OrderPersistenceMapper;
import com.example.ecommerce_clean.modules.order.infrastructure.persistence.repository.OrderJpaRepository;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.entity.ProductJpaEntity;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.repository.ProductJpaRepository;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.entity.UserJpaEntity;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.repository.UserJpaRepository;
import com.example.ecommerce_clean.shared.enums.OrderStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderPersistenceMapper mapper;
    private final UserJpaRepository userJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity;

        if (order.getId() != null) {
            entity = jpaRepository.findById(order.getId()).orElse(new OrderJpaEntity());
        } else {
            entity = new OrderJpaEntity();
        }

        if (order.getUserId() != null) {
            UserJpaEntity user = userJpaRepository.findById(order.getUserId()).orElse(null);
            entity.setUser(user);
        }

        entity.setTotalPrice(order.getTotalPrice());
        entity.setStatus(order.getStatus());

        // Map order items
        entity.getItems().clear();
        if (order.getItems() != null) {
            for (OrderItem domainItem : order.getItems()) {
                OrderItemJpaEntity itemEntity = new OrderItemJpaEntity();
                itemEntity.setOrder(entity);
                if (domainItem.getProductId() != null) {
                    ProductJpaEntity product = productJpaRepository.findById(domainItem.getProductId()).orElse(null);
                    itemEntity.setProduct(product);
                }
                itemEntity.setQuantity(domainItem.getQuantity());
                itemEntity.setPrice(domainItem.getPrice());
                itemEntity.setTotalPrice(domainItem.getTotalPrice());
                entity.getItems().add(itemEntity);
            }
        }

        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Page<Order> findByUserId(Long userId, Pageable pageable) {
        UserJpaEntity user = userJpaRepository.findById(userId).orElse(null);
        if (user == null) return Page.empty();
        return jpaRepository.findByUser(user, pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Order> findByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable) {
        UserJpaEntity user = userJpaRepository.findById(userId).orElse(null);
        if (user == null) return Page.empty();
        return jpaRepository.findByUserAndStatus(user, status, pageable).map(mapper::toDomain);
    }
}

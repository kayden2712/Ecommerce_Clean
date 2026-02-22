package com.example.ecommerce_clean.modules.order.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_clean.modules.order.infrastructure.persistence.entity.OrderItemJpaEntity;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItemJpaEntity, Long> {

    List<OrderItemJpaEntity> findByOrderId(Long orderId);
}

package com.example.ecommerce_clean.modules.order.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_clean.modules.order.infrastructure.persistence.entity.OrderJpaEntity;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.entity.UserJpaEntity;
import com.example.ecommerce_clean.shared.enums.OrderStatus;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {

    Page<OrderJpaEntity> findByUser(UserJpaEntity user, Pageable pageable);

    Page<OrderJpaEntity> findByUserAndStatus(UserJpaEntity user, OrderStatus status, Pageable pageable);
}

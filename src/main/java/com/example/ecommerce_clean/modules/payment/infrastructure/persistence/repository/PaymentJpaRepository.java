package com.example.ecommerce_clean.modules.payment.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_clean.modules.payment.infrastructure.persistence.entity.PaymentJpaEntity;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, Long> {

    Optional<PaymentJpaEntity> findByOrderId(Long orderId);

    Optional<PaymentJpaEntity> findByTransactionId(String transactionId);
}

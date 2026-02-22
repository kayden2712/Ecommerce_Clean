package com.example.ecommerce_clean.modules.payment.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.order.infrastructure.persistence.entity.OrderJpaEntity;
import com.example.ecommerce_clean.modules.order.infrastructure.persistence.repository.OrderJpaRepository;
import com.example.ecommerce_clean.modules.payment.domain.entity.Payment;
import com.example.ecommerce_clean.modules.payment.domain.repository.PaymentRepository;
import com.example.ecommerce_clean.modules.payment.infrastructure.persistence.entity.PaymentJpaEntity;
import com.example.ecommerce_clean.modules.payment.infrastructure.persistence.mapper.PaymentPersistenceMapper;
import com.example.ecommerce_clean.modules.payment.infrastructure.persistence.repository.PaymentJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentPersistenceMapper paymentPersistenceMapper;
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        return paymentJpaRepository.findByOrderId(orderId)
                .map(paymentPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Payment> findByTransactionId(String transactionId) {
        return paymentJpaRepository.findByTransactionId(transactionId)
                .map(paymentPersistenceMapper::toDomain);
    }

    @Override
    public Payment save(Payment payment) {
        PaymentJpaEntity jpaEntity = paymentPersistenceMapper.toJpaEntity(payment);

        if (payment.getOrderId() != null) {
            OrderJpaEntity orderJpa = orderJpaRepository.findById(payment.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("Order not found: " + payment.getOrderId()));
            jpaEntity.setOrder(orderJpa);
        }

        PaymentJpaEntity saved = paymentJpaRepository.save(jpaEntity);
        return paymentPersistenceMapper.toDomain(saved);
    }
}

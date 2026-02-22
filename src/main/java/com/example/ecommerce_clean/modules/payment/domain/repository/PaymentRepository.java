package com.example.ecommerce_clean.modules.payment.domain.repository;

import java.util.Optional;

import com.example.ecommerce_clean.modules.payment.domain.entity.Payment;

public interface PaymentRepository {

    Optional<Payment> findByOrderId(Long orderId);

    Optional<Payment> findByTransactionId(String transactionId);

    Payment save(Payment payment);
}

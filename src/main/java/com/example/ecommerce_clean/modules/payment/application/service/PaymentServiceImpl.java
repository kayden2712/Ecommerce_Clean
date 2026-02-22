package com.example.ecommerce_clean.modules.payment.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce_clean.common.exception.domain.ResourceNotFoundException;
import com.example.ecommerce_clean.modules.order.domain.entity.Order;
import com.example.ecommerce_clean.modules.order.domain.repository.OrderRepository;
import com.example.ecommerce_clean.modules.payment.application.dto.PaymentRequest;
import com.example.ecommerce_clean.modules.payment.application.dto.PaymentResponse;
import com.example.ecommerce_clean.modules.payment.domain.entity.Payment;
import com.example.ecommerce_clean.modules.payment.domain.repository.PaymentRepository;
import com.example.ecommerce_clean.shared.enums.ErrorCode;
import com.example.ecommerce_clean.shared.enums.PaymentMethod;
import com.example.ecommerce_clean.shared.enums.PaymentStatus;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    public PaymentResponse initiatePayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ORDER_NOT_FOUND));

        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(request.amount());
        payment.setPaymentMethod(PaymentMethod.valueOf(request.paymentMethod()));
        payment.setStatus(PaymentStatus.INITIATED);
        payment.setTransactionId(UUID.randomUUID().toString());

        Payment saved = paymentRepository.save(payment);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PAYMENT_NOT_FOUND));
        return toResponse(payment);
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getTransactionId()
        );
    }
}

package com.example.ecommerce_clean.modules.payment.application.service;

import com.example.ecommerce_clean.modules.payment.application.dto.PaymentRequest;
import com.example.ecommerce_clean.modules.payment.application.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse initiatePayment(PaymentRequest request);

    PaymentResponse getPaymentByOrderId(Long orderId);
}

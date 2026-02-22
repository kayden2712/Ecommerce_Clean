package com.example.ecommerce_clean.modules.payment.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_clean.modules.payment.application.dto.PaymentRequest;
import com.example.ecommerce_clean.modules.payment.application.dto.PaymentResponse;
import com.example.ecommerce_clean.modules.payment.application.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse initiatePayment(@RequestBody PaymentRequest request) {
        return paymentService.initiatePayment(request);
    }

    @GetMapping("/order/{orderId}")
    public PaymentResponse getPaymentByOrderId(@PathVariable Long orderId) {
        return paymentService.getPaymentByOrderId(orderId);
    }
}

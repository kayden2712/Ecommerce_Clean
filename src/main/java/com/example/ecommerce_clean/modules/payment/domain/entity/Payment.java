package com.example.ecommerce_clean.modules.payment.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.ecommerce_clean.shared.enums.PaymentMethod;
import com.example.ecommerce_clean.shared.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    private Long id;

    private Long orderId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus status;

    private String transactionId;

    // audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

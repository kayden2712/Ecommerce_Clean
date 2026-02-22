package com.example.ecommerce_clean.modules.order.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.ecommerce_clean.shared.enums.OrderStatus;

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
public class Order {

    private Long id;
    private Long userId;
    private String username;
    private BigDecimal totalPrice;
    private OrderStatus status;

    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Soft delete fields
    private boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;
}

package com.example.ecommerce_clean.modules.cart.domain.entity;

import java.time.LocalDateTime;

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
public class CartItem {

    private Long id;
    private Long cartId;
    private Long productId;
    private String productName;
    private java.math.BigDecimal productPrice;
    private String productColor;
    private Integer quantity;
    private String selectedColor;
    private Integer productStock;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Soft delete fields
    private boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;
}

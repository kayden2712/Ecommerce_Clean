package com.example.ecommerce_clean.modules.product.domain.entity;

import java.math.BigDecimal;
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
public class Product {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String color;
    private Category category;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Soft delete fields
    private boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;
}

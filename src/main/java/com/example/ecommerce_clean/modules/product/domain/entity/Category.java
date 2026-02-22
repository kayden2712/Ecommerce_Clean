package com.example.ecommerce_clean.modules.product.domain.entity;

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
public class Category {

    private Long id;
    private String name;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

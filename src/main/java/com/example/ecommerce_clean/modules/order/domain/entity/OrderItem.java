package com.example.ecommerce_clean.modules.order.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class OrderItem {

    private Long id;
    private Long orderId;
    private final Long productId;
    private final String productName;
    private final Integer quantity;
    private final BigDecimal price;
    private final BigDecimal totalPrice;

    // Audit fields
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Soft delete fields
    private boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;

    // Private constructor for domain control
    private OrderItem(Long id, Long productId, String productName, Integer quantity,
            BigDecimal price, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateInputs(productId, productName, quantity, price);

        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = calculateTotalPrice();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory method to create a new OrderItem
    public static OrderItem create(Long productId, String productName, Integer quantity, BigDecimal price) {
        LocalDateTime now = LocalDateTime.now();
        return new OrderItem(null, productId, productName, quantity, price, now, now);
    }

    // Factory method to reconstitute OrderItem from persistence
    public static OrderItem reconstitute(Long id, Long productId, String productName,
            Integer quantity, BigDecimal price,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new OrderItem(id, productId, productName, quantity, price, createdAt, updatedAt);
    }

    // Calculate total price for this item
    private BigDecimal calculateTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    // Validate business rules
    private void validateInputs(Long productId, String productName, Integer quantity, BigDecimal price) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be null or negative");
        }
    }
}

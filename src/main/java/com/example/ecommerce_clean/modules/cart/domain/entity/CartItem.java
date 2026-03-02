package com.example.ecommerce_clean.modules.cart.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CartItem {

    private Long id;
    private Long cartId;
    private final Long productId;
    private final String productName;
    private final BigDecimal productPrice;
    private final String productColor;
    private Integer quantity;
    private final String selectedColor;
    private final Integer productStock;

    // Audit fields
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Soft delete fields
    private boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;

    // Private constructor for domain control
    private CartItem(Long id, Long productId, String productName, BigDecimal productPrice,
            String productColor, Integer quantity, String selectedColor, Integer productStock,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateInputs(productId, productName, productPrice, quantity, productStock);

        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productColor = productColor;
        this.quantity = quantity;
        this.selectedColor = selectedColor;
        this.productStock = productStock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory method to create a new cart item
    public static CartItem create(Long productId, String productName, BigDecimal productPrice,
            String productColor, Integer quantity, String selectedColor, Integer productStock) {
        LocalDateTime now = LocalDateTime.now();
        return new CartItem(null, productId, productName, productPrice, productColor,
                quantity, selectedColor, productStock, now, now);
    }

    // Factory method to reconstitute cart item from persistence
    public static CartItem reconstitute(Long id, Long productId, String productName, BigDecimal productPrice,
            String productColor, Integer quantity, String selectedColor, Integer productStock,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new CartItem(id, productId, productName, productPrice, productColor,
                quantity, selectedColor, productStock, createdAt, updatedAt);
    }

    // Update quantity
    public void updateQuantity(Integer newQuantity) {
        if (newQuantity == null || newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (newQuantity > productStock) {
            throw new IllegalArgumentException("Quantity exceeds available stock");
        }
        this.quantity = newQuantity;
        this.updatedAt = LocalDateTime.now();
    }

    // Validate business rules
    private void validateInputs(Long productId, String productName, BigDecimal productPrice, Integer quantity,
            Integer productStock) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (productPrice == null || productPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be null or negative");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (productStock == null || productStock < 0) {
            throw new IllegalArgumentException("Product stock cannot be null or negative");
        }
        if (quantity > productStock) {
            throw new IllegalArgumentException("Quantity exceeds available stock");
        }
    }
}

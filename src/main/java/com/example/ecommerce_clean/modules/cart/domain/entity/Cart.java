package com.example.ecommerce_clean.modules.cart.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.ecommerce_clean.common.exception.domain.InvalidOperationException;
import com.example.ecommerce_clean.shared.enums.ErrorCode;

import lombok.Getter;

@Getter
public class Cart {

    private Long id;
    private final Long userId;
    private final List<CartItem> items = new ArrayList<>();

    // Audit fields
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Soft delete fields
    private boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;

    // Private constructor for domain control
    private Cart(Long id, Long userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory method to create a new cart
    public static Cart create(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return new Cart(null, userId, now, now);
    }

    // Factory method to reconstitute cart from persistence
    public static Cart reconstitute(Long id, Long userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Cart(id, userId, createdAt, updatedAt);
    }

    // Reconstitute with items
    public static Cart reconstitute(Long id, Long userId, List<CartItem> items,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        Cart cart = new Cart(id, userId, createdAt, updatedAt);
        if (items != null) {
            cart.items.addAll(items);
        }
        return cart;
    }

    // Add item to cart with validation
    // If item already exists, increase quantity
    public void addItem(Long productId, String productName, BigDecimal productPrice,
            String productColor, Integer productStock, Integer quantity, String selectedColor) {
        validateNotDeleted();
        validateQuantity(quantity);
        validateProductStock(productStock, quantity);

        Optional<CartItem> existingItem = findItemByProductId(productId);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;
            validateProductStock(productStock, newQuantity);
            item.updateQuantity(newQuantity);
        } else {
            CartItem newItem = CartItem.create(
                    productId,
                    productName,
                    productPrice,
                    productColor,
                    quantity,
                    selectedColor,
                    productStock);
            items.add(newItem);
        }

        this.updatedAt = LocalDateTime.now();
    }

    // Update item quantity with validation
    public void updateItemQuantity(Long productId, Integer newQuantity, Integer productStock) {
        validateNotDeleted();
        validateQuantity(newQuantity);
        validateProductStock(productStock, newQuantity);

        CartItem item = findItemByProductId(productId)
                .orElseThrow(() -> new InvalidOperationException(
                        ErrorCode.CART_ITEM_NOT_FOUND,
                        "Product not found in cart"));

        item.updateQuantity(newQuantity);
        this.updatedAt = LocalDateTime.now();
    }

    // Remove item from cart
    public void removeItem(Long productId) {
        validateNotDeleted();

        boolean removed = items.removeIf(item -> item.getProductId().equals(productId) && !item.isDeleted());

        if (!removed) {
            throw new InvalidOperationException(
                    ErrorCode.CART_ITEM_NOT_FOUND,
                    "Product not found in cart");
        }

        this.updatedAt = LocalDateTime.now();
    }

    // Clear all items from cart
    public void clearAllItems() {
        validateNotDeleted();
        items.clear();
        this.updatedAt = LocalDateTime.now();
    }

    // Calculate total cart value
    public BigDecimal calculateTotal() {
        return items.stream()
                .filter(item -> !item.isDeleted())
                .map(item -> item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Get total number of items in cart
    public int getTotalItemCount() {
        return items.stream()
                .filter(item -> !item.isDeleted())
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    // Check if cart is empty
    public boolean isEmpty() {
        return items.stream()
                .noneMatch(item -> !item.isDeleted());
    }

    // Get immutable list of items
    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    // Update timestamp
    public void markAsUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

    // Set ID after persistence (used by repository)
    public void setId(Long id) {
        this.id = id;
    }

    // Private helper methods
    private Optional<CartItem> findItemByProductId(Long productId) {
        return items.stream()
                .filter(item -> item.getProductId().equals(productId) && !item.isDeleted())
                .findFirst();
    }

    private void validateNotDeleted() {
        if (this.deleted) {
            throw new InvalidOperationException(
                    ErrorCode.INVALID_OPERATION,
                    "Cannot modify deleted cart");
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidOperationException(
                    ErrorCode.INVALID_QUANTITY,
                    "Quantity must be greater than 0");
        }
    }

    private void validateProductStock(Integer stock, Integer requestedQuantity) {
        if (stock < requestedQuantity) {
            throw new InvalidOperationException(
                    ErrorCode.INSUFFICIENT_STOCK,
                    "Insufficient stock. Available: " + stock + ", Requested: " + requestedQuantity);
        }
    }
}

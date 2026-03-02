package com.example.ecommerce_clean.modules.order.domain.model;

import java.time.LocalDateTime;


//  Sealed interface representing Order state with type-safe pattern matching
//  Better than enum when each state needs specific data
 
public sealed interface OrderState {
    
    
    //  Order created but not yet paid
    record Pending(LocalDateTime createdAt) implements OrderState {
        public Pending {
            if (createdAt == null) createdAt = LocalDateTime.now();
        }
    }
    
    
    //  Payment received, waiting for processing
    record Paid(
        LocalDateTime paidAt,
        String paymentId
    ) implements OrderState {
        public Paid {
            if (paidAt == null) throw new IllegalArgumentException("Paid date required");
            if (paymentId == null) throw new IllegalArgumentException("Payment ID required");
        }
    }
    
    
    //  Order shipped to customer  
    record Shipped(
        LocalDateTime shippedAt,
        String trackingNumber,
        String carrier
    ) implements OrderState {
        public Shipped {
            if (shippedAt == null) throw new IllegalArgumentException("Shipped date required");
            if (trackingNumber == null) throw new IllegalArgumentException("Tracking number required");
        }
    }
    
    
    //  Order delivered successfully
    record Completed(
        LocalDateTime completedAt,
        LocalDateTime deliveredAt
    ) implements OrderState {
        public Completed {
            if (completedAt == null) completedAt = LocalDateTime.now();
        }
    }
    
    
    //  Order cancelled before shipping
    record Cancelled(
        LocalDateTime cancelledAt,
        String reason,
        String cancelledBy
    ) implements OrderState {
        public Cancelled {
            if (cancelledAt == null) cancelledAt = LocalDateTime.now();
            if (reason == null) reason = "User cancelled";
        }
    }
    
    // Helper methods for state checking
    default boolean isPending() {
        return this instanceof Pending;
    }
    
    default boolean isPaid() {
        return this instanceof Paid;
    }
    
    default boolean isShipped() {
        return this instanceof Shipped;
    }
    
    default boolean isCompleted() {
        return this instanceof Completed;
    }
    
    default boolean isCancelled() {
        return this instanceof Cancelled;
    }
    
    default boolean canBeCancelled() {
        return this instanceof Pending || this instanceof Paid;
    }
    
    default boolean canBeShipped() {
        return this instanceof Paid;
    }
}

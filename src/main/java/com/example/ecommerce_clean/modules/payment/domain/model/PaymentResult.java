package com.example.ecommerce_clean.modules.payment.domain.model;

import java.time.LocalDateTime;

import com.example.ecommerce_clean.shared.domain.valueobject.Money;


/**
 * Sealed interface representing Payment result with type-safe data
 * Uses pattern matching for exhaustive handling
 */
public sealed interface PaymentResult {
    
    //  Payment processed successfully
    record Success(
        String transactionId,
        Money amount,
        LocalDateTime processedAt,
        String gatewayResponse
    ) implements PaymentResult {
        public Success {
            if (transactionId == null) throw new IllegalArgumentException("Transaction ID required");
            if (amount == null) throw new IllegalArgumentException("Amount required");
            if (processedAt == null) processedAt = LocalDateTime.now();
        }
    }
    
    //  Payment failed - includes error details
    record Failed(
        String errorCode,
        String errorMessage,
        LocalDateTime failedAt,
        boolean retryable
    ) implements PaymentResult {
        public Failed {
            if (errorCode == null) errorCode = "UNKNOWN_ERROR";
            if (errorMessage == null) errorMessage = "Payment processing failed";
            if (failedAt == null) failedAt = LocalDateTime.now();
        }
    }
    
    //  Payment is being processed asynchronously
    record Pending(
        String referenceId,
        String gatewayUrl,
        LocalDateTime expiresAt
    ) implements PaymentResult {
        public Pending {
            if (referenceId == null) throw new IllegalArgumentException("Reference ID required");
        }
        
        public boolean isExpired() {
            return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
        }
    }
    
    //  Payment cancelled by user or system
    record Cancelled(
        String reason,
        LocalDateTime cancelledAt,
        String cancelledBy
    ) implements PaymentResult {
        public Cancelled {
            if (cancelledAt == null) cancelledAt = LocalDateTime.now();
            if (reason == null) reason = "Cancelled by user";
        }
    }
    
    // Helper methods for type checking
    default boolean isSuccess() {
        return this instanceof Success;
    }
    
    default boolean isFailed() {
        return this instanceof Failed;
    }
    
    default boolean isPending() {
        return this instanceof Pending;
    }
    
    default boolean isCancelled() {
        return this instanceof Cancelled;
    }
    
    /**
     * Pattern matching example for getting user-friendly message
     */
    default String toUserMessage() {
        return switch (this) {
            case Success(var txId, var amt, var payTime, var gateway) -> 
                "Payment successful via " + gateway + "! Transaction: " + txId + " - Amount: " + amt + " at " + payTime.toLocalTime();
            case Failed(var errorCode, var msg, var failTime, var retryable) -> 
                retryable ? "Payment failed [" + errorCode + "] at " + failTime.toLocalTime() + ": " + msg + " (Please try again)" 
                          : "Payment failed [" + errorCode + "]: " + msg;
            case Pending(var ref, var url, var expires) -> 
                "Payment pending - Reference: " + ref + 
                (url != null ? " URL: " + url : "") +
                (expires != null ? " (Expires: " + expires + ")" : "");
            case Cancelled(var reason, var cancelTime, var by) -> 
                "Payment cancelled by " + by + " at " + cancelTime.toLocalTime() + ": " + reason;
        };
    }
}

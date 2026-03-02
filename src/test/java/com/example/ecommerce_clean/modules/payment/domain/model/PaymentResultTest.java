package com.example.ecommerce_clean.modules.payment.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.example.ecommerce_clean.shared.domain.valueobject.Money;

class PaymentResultTest {
    
    @Test
    void shouldCreateSuccessResult() {
        Money amount = Money.of(BigDecimal.valueOf(100));
        PaymentResult result = new PaymentResult.Success(
            "TXN123",
            amount,
            LocalDateTime.now(),
            "OK"
        );
        
        assertTrue(result.isSuccess());
        assertFalse(result.isFailed());
        
        // Pattern matching
        String message = result.toUserMessage();
        assertTrue(message.contains("successful"));
        assertTrue(message.contains("TXN123"));
    }
    
    @Test
    void shouldCreateFailedResult() {
        PaymentResult result = new PaymentResult.Failed(
            "INSUFFICIENT_FUNDS",
            "Insufficient balance",
            LocalDateTime.now(),
            true
        );
        
        assertTrue(result.isFailed());
        assertFalse(result.isSuccess());
        
        String message = result.toUserMessage();
        assertTrue(message.contains("failed"));
        assertTrue(message.contains("try again"));
    }
    
    @Test
    void shouldCreatePendingResult() {
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);
        PaymentResult result = new PaymentResult.Pending(
            "REF123",
            "https://gateway.com/pay",
            expiresAt
        );
        
        assertTrue(result.isPending());
        assertFalse(((PaymentResult.Pending) result).isExpired());
    }
    
    @Test
    void shouldDetectExpiredPending() {
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(1);
        PaymentResult.Pending result = new PaymentResult.Pending(
            "REF123",
            "https://gateway.com/pay",
            expiredTime
        );
        
        assertTrue(result.isExpired());
    }
    
    @Test
    void shouldCreateCancelledResult() {
        PaymentResult result = new PaymentResult.Cancelled(
            "User cancelled",
            LocalDateTime.now(),
            "user123"
        );
        
        assertTrue(result.isCancelled());
        assertFalse(result.isSuccess());
    }
    
    @Test
    void shouldUsePatternMatchingExhaustively() {
        PaymentResult[] results = {
            new PaymentResult.Success("TX1", Money.of(BigDecimal.TEN), LocalDateTime.now(), "OK"),
            new PaymentResult.Failed("ERR", "Error", LocalDateTime.now(), false),
            new PaymentResult.Pending("REF1", "url", null),
            new PaymentResult.Cancelled("reason", LocalDateTime.now(), "user")
        };
        
        // Test that switch expression handles all cases
        for (PaymentResult result : results) {
            String message = switch (result) {
                case PaymentResult.Success s -> "Success: " + s.transactionId();
                case PaymentResult.Failed f -> "Failed: " + f.errorCode();
                case PaymentResult.Pending p -> "Pending: " + p.referenceId();
                case PaymentResult.Cancelled c -> "Cancelled: " + c.reason();
            };
            
            assertNotNull(message);
            assertFalse(message.isEmpty());
        }
    }
    
    @Test
    void shouldThrowForMissingRequiredFields() {
        assertThrows(IllegalArgumentException.class, () ->
            new PaymentResult.Success(null, Money.of(BigDecimal.TEN), LocalDateTime.now(), "OK")
        );
        
        assertThrows(IllegalArgumentException.class, () ->
            new PaymentResult.Pending(null, "url", null)
        );
    }
    
    @Test
    void shouldProvideDefaultValues() {
        PaymentResult.Failed failed = new PaymentResult.Failed(null, null, null, false);
        
        assertEquals("UNKNOWN_ERROR", failed.errorCode());
        assertEquals("Payment processing failed", failed.errorMessage());
        assertNotNull(failed.failedAt());
    }
}

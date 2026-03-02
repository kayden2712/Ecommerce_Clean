package com.example.ecommerce_clean.shared.domain.valueobject;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class MoneyTest {
    
    @Test
    void shouldCreateMoneyWithValidAmount() {
        Money money = Money.of(BigDecimal.valueOf(100.50));
        
        assertEquals(BigDecimal.valueOf(100.50).setScale(2), money.amount());
        assertEquals("VND", money.currency());
    }
    
    @Test
    void shouldThrowExceptionForNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> 
            Money.of(BigDecimal.valueOf(-10))
        );
    }
    
    @Test
    void shouldThrowExceptionForNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Money(null, "VND")
        );
    }
    
    @Test
    void shouldThrowExceptionForInvalidCurrency() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Money(BigDecimal.TEN, "INVALID")
        );
    }
    
    @Test
    void shouldAddMoneyCorrectly() {
        Money money1 = Money.of(BigDecimal.valueOf(100));
        Money money2 = Money.of(BigDecimal.valueOf(50));
        
        Money result = money1.add(money2);
        
        assertEquals(BigDecimal.valueOf(150.00).setScale(2), result.amount());
    }
    
    @Test
    void shouldThrowExceptionWhenAddingDifferentCurrencies() {
        Money vnd = Money.of(BigDecimal.valueOf(100), "VND");
        Money usd = Money.of(BigDecimal.valueOf(10), "USD");
        
        assertThrows(IllegalArgumentException.class, () -> 
            vnd.add(usd)
        );
    }
    
    @Test
    void shouldSubtractMoneyCorrectly() {
        Money money1 = Money.of(BigDecimal.valueOf(100));
        Money money2 = Money.of(BigDecimal.valueOf(30));
        
        Money result = money1.subtract(money2);
        
        assertEquals(BigDecimal.valueOf(70.00).setScale(2), result.amount());
    }
    
    @Test
    void shouldThrowExceptionForNegativeSubtractionResult() {
        Money money1 = Money.of(BigDecimal.valueOf(50));
        Money money2 = Money.of(BigDecimal.valueOf(100));
        
        assertThrows(IllegalArgumentException.class, () -> 
            money1.subtract(money2)
        );
    }
    
    @Test
    void shouldMultiplyByQuantity() {
        Money money = Money.of(BigDecimal.valueOf(25.50));
        
        Money result = money.multiply(3);
        
        assertEquals(BigDecimal.valueOf(76.50).setScale(2), result.amount());
    }
    
    @Test
    void shouldCompareMoneyCorrectly() {
        Money smaller = Money.of(BigDecimal.valueOf(50));
        Money larger = Money.of(BigDecimal.valueOf(100));
        
        assertTrue(smaller.isLessThan(larger));
        assertTrue(larger.isGreaterThan(smaller));
        assertFalse(smaller.isGreaterThan(larger));
    }
    
    @Test
    void shouldCheckIfZero() {
        Money zero = Money.of(BigDecimal.ZERO);
        Money nonZero = Money.of(BigDecimal.TEN);
        
        assertTrue(zero.isZero());
        assertFalse(nonZero.isZero());
    }
    
    @Test
    void shouldNormalizeDecimalPlaces() {
        Money money = new Money(BigDecimal.valueOf(100.123456), "VND");
        
        // Should be rounded to 2 decimal places
        assertEquals(BigDecimal.valueOf(100.12).setScale(2), money.amount());
    }
    
    @Test
    void shouldBeImmutable() {
        Money original = Money.of(BigDecimal.valueOf(100));
        Money result = original.add(Money.of(BigDecimal.valueOf(50)));
        
        // Original should not change
        assertEquals(BigDecimal.valueOf(100.00).setScale(2), original.amount());
        assertEquals(BigDecimal.valueOf(150.00).setScale(2), result.amount());
    }
}

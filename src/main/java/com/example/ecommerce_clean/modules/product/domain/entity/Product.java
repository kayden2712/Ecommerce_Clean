package com.example.ecommerce_clean.modules.product.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.ecommerce_clean.common.exception.domain.InvalidOperationException;
import com.example.ecommerce_clean.shared.domain.valueobject.Money;
import com.example.ecommerce_clean.shared.enums.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Product {

    private Long id;
    private String name;
    private Money price;
    private Integer stock;
    private String color;
    private Category category;

    // Audit fields
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Soft delete fields
    private boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;

    private Product(String name, Money price, Integer stock, String color, Category category) {
        validateName(name);
        validatePrice(price);
        validateStock(stock);
        validateCategory(category);

        this.name = name;
        this.price = price;
        this.stock = stock;
        this.color = color;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    public static Product create(String name, BigDecimal price, Integer stock,
            String color, Category category) {
        Money productPrice = Money.of(price);
        return new Product(name, productPrice, stock, color, category);
    }

    // Update price
    public void updatePrice(BigDecimal newPrice) {
        validateNotDeleted();
        Money updatedPrice = Money.of(newPrice);
        validatePrice(updatedPrice);
        this.price = updatedPrice;
        this.updatedAt = LocalDateTime.now();
    }

    // Reconstitute aggregate from persisted data
    public static Product reconstitute(
            Long id,
            String name,
            BigDecimal price,
            Integer stock,
            String color,
            Category category,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            boolean deleted,
            LocalDateTime deletedAt,
            String deletedBy) {
        Money productPrice = Money.of(price);
        Product product = new Product(name, productPrice, stock, color, category);
        product.id = id;
        product.updatedAt = updatedAt;
        product.deleted = deleted;
        product.deletedAt = deletedAt;
        product.deletedBy = deletedBy;
        return product;
    }

    // Update all product information
    public void updateInformation(String name, BigDecimal price, Integer stock, String color, Category category) {
        validateNotDeleted();
        validateName(name);
        validatePrice(Money.of(price));
        validateStock(stock);
        validateCategory(category);

        this.name = name;
        this.price = Money.of(price);
        this.stock = stock;
        this.color = color;
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    // Change product price with validation
    public void changePrice(BigDecimal newPrice) {
        validateNotDeleted();
        Money newPriceValue = Money.of(newPrice);
        validatePrice(newPriceValue);

        this.price = newPriceValue;
        this.updatedAt = LocalDateTime.now();
    }

    // Update only basic information (name, color, category)
    public void updateBasicInfo(String name, String color, Category category) {
        validateNotDeleted();
        validateName(name);
        validateCategory(category);

        this.name = name;
        this.color = color;
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    // Increase stock quantity
    public void increaseStock(int quantity) {
        validateNotDeleted();
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to increase must be greater than 0");
        }
        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    // Decrease stock quantity
    public void decreaseStock(int quantity) {
        validateNotDeleted();
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to decrease must be greater than 0");
        }
        if (this.stock < quantity) {
            throw new InvalidOperationException(ErrorCode.INVALID_INPUT, "Cannot decrease stock below zero");
        }
        this.stock -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    
    //  Reserve stock for order (optimistic approach)
    public void reserveStock(int quantity) {
        decreaseStock(quantity); // Uses same validation logic
    }

    
    //  Release reserved stock (e.g., when order is cancelled)
    public void releaseStock(int quantity) {
        increaseStock(quantity); // Uses same validation logic
    }

    
    //  Check if product has sufficient stock
    public boolean hasInStock(int requestedQuantity) {
        return this.stock >= requestedQuantity;
    }

    
    //  Check if product is out of stock
    public boolean isOutOfStock() {
        return this.stock <= 0;
    }

    
    //  Check if product is low in stock
    public boolean isLowInStock(int threshold) {
        return this.stock <= threshold && this.stock > 0;
    }

    
    //  Soft delete product
    public void softDelete(String deletedBy) {
        if (this.deleted) {
            throw new InvalidOperationException(
                    ErrorCode.INVALID_OPERATION,
                    "Product is already deleted");
        }

        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.updatedAt = LocalDateTime.now();
    }

    
    //  Restore soft-deleted product
    public void restore() {
        if (!this.deleted) {
            throw new InvalidOperationException(
                    ErrorCode.INVALID_OPERATION,
                    "Product is not deleted");
        }

        this.deleted = false;
        this.deletedAt = null;
        this.deletedBy = null;
        this.updatedAt = LocalDateTime.now();
    }
    // Validation methods

    private void validateNotDeleted() {
        if (this.deleted) {
            throw new InvalidOperationException(ErrorCode.INVALID_OPERATION, "Cannot modify deleted product");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Product name cannot exceed 255 characters");
        }
    }

    private void validatePrice(Money price) {
        if (price == null) {
            throw new IllegalArgumentException("Product price cannot be null");
        }
        if (price.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException(
                    ErrorCode.INVALID_INPUT,
                    "Product price must be greater than 0");
        }
    }

    private void validateStock(Integer stock) {
        if (stock == null) {
            throw new IllegalArgumentException("Product stock cannot be null");
        }
        if (stock < 0) {
            throw new InvalidOperationException(ErrorCode.INVALID_INPUT, "Product stock cannot be negative");
        }
    }

    private void validateCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Product category cannot be null");
        }
    }
}

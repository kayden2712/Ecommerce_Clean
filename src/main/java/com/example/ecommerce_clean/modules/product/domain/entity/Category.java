package com.example.ecommerce_clean.modules.product.domain.entity;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class Category {

    private Long id;
    private String name;

    // Audit fields
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Private constructor for domain control
    private Category(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //  Factory method to create a new category
    public static Category create(String name) {
        LocalDateTime now = LocalDateTime.now();
        return new Category(null, name, now, now);
    }

    //  Factory method to reconstitute category from persistence
    public static Category reconstitute(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Category(id, name, createdAt, updatedAt);
    }
    
    //  Update category name
    public void updateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }
}

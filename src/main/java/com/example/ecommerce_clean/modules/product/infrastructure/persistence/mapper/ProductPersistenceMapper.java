package com.example.ecommerce_clean.modules.product.infrastructure.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.product.domain.entity.Product;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.entity.ProductJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductPersistenceMapper {

    private final CategoryPersistenceMapper categoryMapper;

    // Convert ProductJpaEntity to Product domain entity
    public Product toDomain(ProductJpaEntity entity) {
        if (entity == null)
            return null;

        return Product.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getStock(),
                entity.getColor(),
                categoryMapper.toDomain(entity.getCategory()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isDeleted(),
                entity.getDeletedAt(),
                entity.getDeletedBy());
    }

    // Convert Product domain entity to JPA entity
    public ProductJpaEntity toJpaEntity(Product product) {
        if (product == null)
            return null;

        ProductJpaEntity entity = new ProductJpaEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setPrice(product.getPrice().amount()); // Extract BigDecimal from Money
        entity.setStock(product.getStock());
        entity.setColor(product.getColor());
        entity.setCategory(categoryMapper.toJpaEntity(product.getCategory()));
        return entity;
    }

    // Convert list of ProductJpaEntity to list of Product domain entities
    public List<Product> toDomainList(List<ProductJpaEntity> entities) {
        if (entities == null)
            return null;

        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}

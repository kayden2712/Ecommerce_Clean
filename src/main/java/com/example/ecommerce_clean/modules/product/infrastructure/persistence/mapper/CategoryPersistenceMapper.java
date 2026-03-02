package com.example.ecommerce_clean.modules.product.infrastructure.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.product.domain.entity.Category;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.entity.CategoryJpaEntity;

@Component
public class CategoryPersistenceMapper {

    //  Convert CategoryJpaEntity to Category domain entity
    public Category toDomain(CategoryJpaEntity entity) {
        if (entity == null) return null;
        
        return Category.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
    
    //  Convert Category domain entity to JPA entity
    public CategoryJpaEntity toJpaEntity(Category category) {
        if (category == null) return null;
        
        CategoryJpaEntity entity = new CategoryJpaEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        return entity;
    }

    //  Convert list of CategoryJpaEntity to list of Category domain entities
    public List<Category> toDomainList(List<CategoryJpaEntity> entities) {
        if (entities == null) return null;
        
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}

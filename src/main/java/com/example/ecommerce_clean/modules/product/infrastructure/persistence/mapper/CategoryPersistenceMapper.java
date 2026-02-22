package com.example.ecommerce_clean.modules.product.infrastructure.persistence.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.ecommerce_clean.modules.product.domain.entity.Category;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.entity.CategoryJpaEntity;

@Mapper(componentModel = "spring")
public interface CategoryPersistenceMapper {

    Category toDomain(CategoryJpaEntity entity);

    CategoryJpaEntity toJpaEntity(Category category);

    List<Category> toDomainList(List<CategoryJpaEntity> entities);
}

package com.example.ecommerce_clean.modules.product.infrastructure.persistence.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.ecommerce_clean.modules.product.domain.entity.Product;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.entity.ProductJpaEntity;

@Mapper(componentModel = "spring", uses = CategoryPersistenceMapper.class)
public interface ProductPersistenceMapper {

    Product toDomain(ProductJpaEntity entity);

    ProductJpaEntity toJpaEntity(Product product);

    List<Product> toDomainList(List<ProductJpaEntity> entities);
}

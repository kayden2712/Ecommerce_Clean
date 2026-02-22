package com.example.ecommerce_clean.modules.product.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.ecommerce_clean.modules.product.application.dto.CreateProductRequest;
import com.example.ecommerce_clean.modules.product.application.dto.ProductResponse;
import com.example.ecommerce_clean.modules.product.application.dto.UpdateProductRequest;
import com.example.ecommerce_clean.modules.product.domain.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Product toEntity(CreateProductRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(UpdateProductRequest request, @MappingTarget Product product);

    @Mapping(source = "category.name", target = "categoryName")
    ProductResponse toResponse(Product product);
}

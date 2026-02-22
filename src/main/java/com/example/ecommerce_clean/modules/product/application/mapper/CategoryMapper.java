package com.example.ecommerce_clean.modules.product.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.ecommerce_clean.modules.product.application.dto.CategoryRequest;
import com.example.ecommerce_clean.modules.product.application.dto.CategoryResponse;
import com.example.ecommerce_clean.modules.product.domain.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(CategoryRequest request, @MappingTarget Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);
}

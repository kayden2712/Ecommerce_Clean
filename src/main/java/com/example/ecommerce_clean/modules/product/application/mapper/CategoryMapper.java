package com.example.ecommerce_clean.modules.product.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.product.application.dto.CategoryRequest;
import com.example.ecommerce_clean.modules.product.application.dto.CategoryResponse;
import com.example.ecommerce_clean.modules.product.domain.entity.Category;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        if (request == null) return null;
        return Category.create(request.name());
    }

    public CategoryResponse toResponse(Category category) {
        if (category == null) return null;
        
        return new CategoryResponse(
            category.getId(),
            category.getName()
        );
    }

    public List<CategoryResponse> toResponseList(List<Category> categories) {
        if (categories == null) return null;
        
        return categories.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
}

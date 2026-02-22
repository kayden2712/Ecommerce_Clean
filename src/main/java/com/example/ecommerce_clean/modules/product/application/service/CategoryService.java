package com.example.ecommerce_clean.modules.product.application.service;

import java.util.List;

import com.example.ecommerce_clean.modules.product.application.dto.CategoryRequest;
import com.example.ecommerce_clean.modules.product.application.dto.CategoryResponse;

public interface CategoryService {

    void validateCategoryExists(Long categoryId);

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);

    List<CategoryResponse> getAll();

    CategoryResponse getById(Long id);
}

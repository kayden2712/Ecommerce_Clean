package com.example.ecommerce_clean.modules.product.domain.repository;

import java.util.List;
import java.util.Optional;

import com.example.ecommerce_clean.modules.product.domain.entity.Category;

public interface CategoryRepository {

    Optional<Category> findById(Long id);

    Category save(Category category);

    void delete(Category category);

    List<Category> findAll();

    boolean existsByName(String name);

    boolean existsById(Long id);
}

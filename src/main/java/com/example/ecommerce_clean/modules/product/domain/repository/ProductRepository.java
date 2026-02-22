package com.example.ecommerce_clean.modules.product.domain.repository;

import java.util.List;
import java.util.Optional;

import com.example.ecommerce_clean.modules.product.domain.entity.Product;

public interface ProductRepository {

    Optional<Product> findById(Long id);

    Product save(Product product);

    void delete(Product product);

    List<Product> findAll();

    boolean existsByName(String name);
}

package com.example.ecommerce_clean.modules.product.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_clean.modules.product.infrastructure.persistence.entity.CategoryJpaEntity;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {

    boolean existsByName(String name);
}

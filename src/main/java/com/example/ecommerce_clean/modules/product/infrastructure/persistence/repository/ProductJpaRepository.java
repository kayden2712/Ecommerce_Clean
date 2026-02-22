package com.example.ecommerce_clean.modules.product.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_clean.modules.product.infrastructure.persistence.entity.ProductJpaEntity;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    boolean existsByName(String name);
}

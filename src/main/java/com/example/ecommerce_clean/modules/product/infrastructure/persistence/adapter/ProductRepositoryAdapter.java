package com.example.ecommerce_clean.modules.product.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.product.domain.entity.Product;
import com.example.ecommerce_clean.modules.product.domain.repository.ProductRepository;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.entity.ProductJpaEntity;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.mapper.ProductPersistenceMapper;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.repository.ProductJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository jpaRepository;
    private final ProductPersistenceMapper mapper;

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = mapper.toJpaEntity(product);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void delete(Product product) {
        jpaRepository.deleteById(product.getId());
    }

    @Override
    public List<Product> findAll() {
        return mapper.toDomainList(jpaRepository.findAll());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }
}

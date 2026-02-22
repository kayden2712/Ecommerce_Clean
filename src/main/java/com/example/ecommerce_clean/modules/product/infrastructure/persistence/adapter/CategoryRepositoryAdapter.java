package com.example.ecommerce_clean.modules.product.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.product.domain.entity.Category;
import com.example.ecommerce_clean.modules.product.domain.repository.CategoryRepository;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.entity.CategoryJpaEntity;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.mapper.CategoryPersistenceMapper;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.repository.CategoryJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;
    private final CategoryPersistenceMapper mapper;

    @Override
    public Optional<Category> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Category save(Category category) {
        CategoryJpaEntity entity = mapper.toJpaEntity(category);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void delete(Category category) {
        jpaRepository.deleteById(category.getId());
    }

    @Override
    public List<Category> findAll() {
        return mapper.toDomainList(jpaRepository.findAll());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}

package com.example.ecommerce_clean.modules.user.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.user.domain.entity.User;
import com.example.ecommerce_clean.modules.user.domain.repository.UserRepository;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.entity.UserJpaEntity;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserPersistenceMapper mapper;

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toJpaEntity(user);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void delete(User user) {
        jpaRepository.deleteById(user.getId());
    }
}

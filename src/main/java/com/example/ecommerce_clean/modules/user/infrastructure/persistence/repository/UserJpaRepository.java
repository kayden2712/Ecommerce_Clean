package com.example.ecommerce_clean.modules.user.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_clean.modules.user.infrastructure.persistence.entity.UserJpaEntity;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}

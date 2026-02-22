package com.example.ecommerce_clean.modules.cart.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.entity.CartJpaEntity;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.entity.UserJpaEntity;

@Repository
public interface CartJpaRepository extends JpaRepository<CartJpaEntity, Long> {

    Optional<CartJpaEntity> findByUser(UserJpaEntity user);
}

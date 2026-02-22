package com.example.ecommerce_clean.modules.cart.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.entity.CartItemJpaEntity;

@Repository
public interface CartItemJpaRepository extends JpaRepository<CartItemJpaEntity, Long> {

    List<CartItemJpaEntity> findByCartId(Long cartId);

    Optional<CartItemJpaEntity> findByCartIdAndProductId(Long cartId, Long productId);

    @Modifying
    @Query("DELETE FROM CartItemJpaEntity ci WHERE ci.cart.id = :cartId")
    void deleteAllByCartId(@Param("cartId") Long cartId);

    List<CartItemJpaEntity> findAllByCartId(Long cartId);
}

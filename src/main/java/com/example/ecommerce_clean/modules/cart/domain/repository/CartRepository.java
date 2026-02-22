package com.example.ecommerce_clean.modules.cart.domain.repository;

import java.util.Optional;

import com.example.ecommerce_clean.modules.cart.domain.entity.Cart;

public interface CartRepository {

    Optional<Cart> findById(Long id);

    Optional<Cart> findByUserId(Long userId);

    Cart save(Cart cart);

    void delete(Cart cart);
}

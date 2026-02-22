package com.example.ecommerce_clean.modules.cart.domain.repository;

import java.util.List;
import java.util.Optional;

import com.example.ecommerce_clean.modules.cart.domain.entity.CartItem;

public interface CartItemRepository {

    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    void deleteAllByCartId(Long cartId);

    List<CartItem> findAllByCartId(Long cartId);

    CartItem save(CartItem cartItem);

    void delete(CartItem cartItem);
}

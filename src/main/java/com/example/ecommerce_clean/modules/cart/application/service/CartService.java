package com.example.ecommerce_clean.modules.cart.application.service;

import java.util.List;

import com.example.ecommerce_clean.modules.cart.application.dto.AddToCartRequest;
import com.example.ecommerce_clean.modules.cart.application.dto.CartItemResponse;

public interface CartService {

    void addToCart(String username, AddToCartRequest request);

    void updateCart(String username, Long productId, Integer quantity);

    CartItemResponse getCartItem(String username, Long productId);

    List<CartItemResponse> getAllCartItems(String username);

    void removeItem(String username, Long productId);

    void clearCart(String username);
}

package com.example.ecommerce_clean.modules.cart.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce_clean.common.exception.domain.ResourceNotFoundException;
import com.example.ecommerce_clean.modules.cart.application.dto.AddToCartRequest;
import com.example.ecommerce_clean.modules.cart.application.dto.CartItemResponse;
import com.example.ecommerce_clean.modules.cart.application.mapper.CartItemMapper;
import com.example.ecommerce_clean.modules.cart.domain.entity.Cart;
import com.example.ecommerce_clean.modules.cart.domain.repository.CartRepository;
import com.example.ecommerce_clean.modules.product.domain.entity.Product;
import com.example.ecommerce_clean.modules.product.domain.repository.ProductRepository;
import com.example.ecommerce_clean.modules.user.domain.entity.User;
import com.example.ecommerce_clean.modules.user.domain.repository.UserRepository;
import com.example.ecommerce_clean.shared.enums.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;
    private final UserRepository userRepository;

    @Override
    public void addToCart(String username, AddToCartRequest request) {
        User user = getUser(username);
        Product product = getProduct(request.productId());
        Cart cart = getOrCreateCart(user);
        
        // Business logic delegated to domain
        cart.addItem(
            product.getId(),
            product.getName(),
            product.getPrice().amount(),
            product.getColor(),
            product.getStock(),
            request.quantity(),
            request.selectedColor()
        );
        
        cartRepository.save(cart);
    }

    @Override
    public void removeItem(String username, Long productId) {
        User user = getUser(username);
        Cart cart = getCartByUserId(user.getId());
        
        // Business logic in domain
        cart.removeItem(productId);
        
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(String username) {
        User user = getUser(username);
        Cart cart = getCartByUserId(user.getId());
        
        // Business logic in domain
        cart.clearAllItems();
        
        cartRepository.save(cart);
    }

    @Override
    public void updateCart(String username, Long productId, Integer quantity) {
        User user = getUser(username);
        Product product = getProduct(productId);
        Cart cart = getCartByUserId(user.getId());
        
        // Business logic in domain with validation
        cart.updateItemQuantity(productId, quantity, product.getStock());
        
        cartRepository.save(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartItemResponse getCartItem(String username, Long productId) {
        User user = getUser(username);
        Cart cart = getCartByUserId(user.getId());
        
        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .map(cartItemMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CART_ITEM_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponse> getAllCartItems(String username) {
        User user = getUser(username);
        Cart cart = getOrCreateCart(user);
        
        return cart.getItems().stream()
                .map(cartItemMapper::toResponse)
                .toList();
    }
    
    // Private helper methods
    
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = Cart.create(user.getId());
                    return cartRepository.save(cart);
                });
    }
    
    private Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CART_NOT_FOUND));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}

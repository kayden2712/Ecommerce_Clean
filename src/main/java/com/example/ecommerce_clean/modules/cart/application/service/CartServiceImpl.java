package com.example.ecommerce_clean.modules.cart.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce_clean.common.exception.domain.InvalidOperationException;
import com.example.ecommerce_clean.common.exception.domain.ResourceNotFoundException;
import com.example.ecommerce_clean.modules.cart.application.dto.AddToCartRequest;
import com.example.ecommerce_clean.modules.cart.application.dto.CartItemResponse;
import com.example.ecommerce_clean.modules.cart.application.mapper.CartItemMapper;
import com.example.ecommerce_clean.modules.cart.domain.entity.Cart;
import com.example.ecommerce_clean.modules.cart.domain.entity.CartItem;
import com.example.ecommerce_clean.modules.cart.domain.repository.CartItemRepository;
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
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;
    private final UserRepository userRepository;

    @Override
    public void addToCart(String username, AddToCartRequest request) {
        User user = getUser(username);
        Product product = getProduct(request.productId());
        Cart cart = getOrCreateCart(user);

        validateQuantity(request.quantity());

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        int newQuantity = (cartItem != null)
                ? cartItem.getQuantity() + request.quantity()
                : request.quantity();

        validateStock(product, newQuantity);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCartId(cart.getId());
            cartItem.setProductId(product.getId());
        }

        cartItem.setQuantity(newQuantity);
        cartItem.setSelectedColor(request.selectedColor());

        cartItemRepository.save(cartItem);
    }

    @Override
    public void removeItem(String username, Long productId) {
        User user = getUser(username);
        Cart cart = getOrCreateCart(user);
        CartItem cartItem = getCartItemByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String username) {
        User user = getUser(username);
        Cart cart = getOrCreateCart(user);
        cartItemRepository.deleteAllByCartId(cart.getId());
    }

    @Override
    public void updateCart(String username, Long productId, Integer quantity) {
        User user = getUser(username);
        Cart cart = getOrCreateCart(user);
        Product product = getProduct(productId);

        validateQuantity(quantity);
        validateStock(product, quantity);

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CART_ITEM_NOT_FOUND));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional(readOnly = true)
    public CartItemResponse getCartItem(String username, Long productId) {
        User user = getUser(username);
        Cart cart = getOrCreateCart(user);
        return cartItemMapper.toResponse(getCartItemByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CART_ITEM_NOT_FOUND)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponse> getAllCartItems(String username) {
        User user = getUser(username);
        Cart cart = getOrCreateCart(user);
        return cartItemRepository.findAllByCartId(cart.getId()).stream()
                .map(cartItemMapper::toResponse)
                .toList();
    }

    // Fetch or create cart for user
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(user.getId());
                    return cartRepository.save(cart);
                });
    }

    // Fetch product by ID
    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    // Fetch user by username
    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    // Fetch cart item by cart ID and product ID
    private Optional<CartItem> getCartItemByCartIdAndProductId(Long cartId, Long productId) {
        return cartItemRepository.findByCartIdAndProductId(cartId, productId);
    }

    // Validate quantity
    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidOperationException(ErrorCode.INVALID_QUANTITY);
        }
    }

    // Validate stock availability
    private void validateStock(Product product, Integer quantity) {
        if (product.getStock() < quantity) {
            throw new InvalidOperationException(ErrorCode.INSUFFICIENT_STOCK);
        }
    }
}

package com.example.ecommerce_clean.modules.cart.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_clean.modules.cart.application.dto.AddToCartRequest;
import com.example.ecommerce_clean.modules.cart.application.dto.CartItemResponse;
import com.example.ecommerce_clean.modules.cart.application.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        cartService.addToCart(authentication.getName(), request);
    }

    @PatchMapping("/{productId}")
    public void updateCart(@PathVariable Long productId, @RequestParam Integer quantity, Authentication authentication) {
        cartService.updateCart(authentication.getName(), productId, quantity);
    }

    @GetMapping("/{productId}")
    public CartItemResponse getCartItem(@PathVariable Long productId, Authentication authentication) {
        return cartService.getCartItem(authentication.getName(), productId);
    }

    @GetMapping
    public List<CartItemResponse> getAll(Authentication authentication) {
        return cartService.getAllCartItems(authentication.getName());
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromCart(@PathVariable Long productId, Authentication authentication) {
        cartService.removeItem(authentication.getName(), productId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
    }
}

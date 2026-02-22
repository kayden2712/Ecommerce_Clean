package com.example.ecommerce_clean.shared.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Authentication & Authorization
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid username/email or password"),
    PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email is already in use"),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Username is already in use"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid or expired token"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "User access denied"),

    // Order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order not found"),
    ORDER_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "Order is already cancelled"),
    ORDER_CANNOT_BE_CANCELLED(HttpStatus.BAD_REQUEST, "Order cannot be cancelled in its current status"),
    ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "User does not have permission to access this order"),

    // Cart
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "Cart not found"),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "Cart item not found"),
    CART_EMPTY(HttpStatus.BAD_REQUEST, "Cart is empty"),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero"),

    // Product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product not found"),
    PRODUCT_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "Product is out of stock"),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "Insufficient stock for product"),

    // Category
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Category not found"),
    CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "Category already exists"),

    // Refresh Token
    REFRESH_TOKEN_REVOKED(HttpStatus.UNAUTHORIZED, "Refresh token has been revoked"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh token has expired"),

    // Payment
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Payment not found"),
    PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "Payment failed"),

    // System
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");

    private final HttpStatus status;
    private final String message;
}

package com.example.ecommerce_clean.modules.user.application.dto;

public record UserResponse(
        Long id,
        String email,
        String username,
        String fullName,
        String role
) {
}

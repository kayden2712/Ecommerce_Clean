package com.example.ecommerce_clean.modules.user.application.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String usernameOrEmail,
        @NotBlank String password
) {
}

package com.example.ecommerce_clean.modules.user.application.service;

import com.example.ecommerce_clean.modules.user.application.dto.AuthResponse;
import com.example.ecommerce_clean.modules.user.application.dto.LoginRequest;
import com.example.ecommerce_clean.modules.user.application.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(String refreshToken);

    void logout(String refreshToken);
}

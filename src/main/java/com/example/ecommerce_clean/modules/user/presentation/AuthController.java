package com.example.ecommerce_clean.modules.user.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_clean.common.security.CookieUtil;
import com.example.ecommerce_clean.modules.user.application.dto.LoginRequest;
import com.example.ecommerce_clean.modules.user.application.dto.RegisterRequest;
import com.example.ecommerce_clean.modules.user.application.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        var authResponse = authService.login(request);
        cookieUtil.createAccessTokenCookie(response, authResponse.accessToken());
        cookieUtil.createRefreshTokenCookie(response, authResponse.refreshToken());
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieUtil.getRefreshToken(request)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        var authResponse = authService.refresh(refreshToken);
        cookieUtil.createAccessTokenCookie(response, authResponse.accessToken());
        return ResponseEntity.ok("Token refreshed successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        cookieUtil.getRefreshToken(request).ifPresent(token -> {
            try {
                authService.logout(token);
            } catch (Exception ignored) {
                // Nếu token đã bị revoke hoặc hết hạn, vẫn xóa cookie
            }
        });
        cookieUtil.deleteAuthCookies(response);
        return ResponseEntity.noContent().build();
    }
}

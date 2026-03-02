package com.example.ecommerce_clean.modules.user.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecommerce_clean.common.exception.domain.DuplicateResourceException;
import com.example.ecommerce_clean.common.exception.domain.ResourceNotFoundException;
import com.example.ecommerce_clean.common.exception.security.UnauthorizedException;
import com.example.ecommerce_clean.common.security.JwtUtil;
import com.example.ecommerce_clean.modules.user.application.dto.AuthResponse;
import com.example.ecommerce_clean.modules.user.application.dto.LoginRequest;
import com.example.ecommerce_clean.modules.user.application.dto.RegisterRequest;
import com.example.ecommerce_clean.modules.user.domain.entity.RefreshToken;
import com.example.ecommerce_clean.modules.user.domain.entity.User;
import com.example.ecommerce_clean.modules.user.domain.repository.UserRepository;
import com.example.ecommerce_clean.shared.enums.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void register(RegisterRequest request) {
        // Check uniqueness (repository concerns)
        validateEmailUniqueness(request.email());
        validateUsernameUniqueness(request.username());
        
        // Validate password before hashing (domain logic)
        User.validateRawPassword(request.password());
        
        // Create user with factory method
        User user = User.create(
            request.email(),
            request.username(),
            passwordEncoder.encode(request.password()),
            request.fullName()
        );
        
        repository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = getUser(request.usernameOrEmail());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = refreshTokenService.create(user).getToken();
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        RefreshToken token = refreshTokenService.verify(refreshToken);
        User user = token.getUser();
        String newToken = jwtUtil.generateToken(user);
        return new AuthResponse(newToken, refreshToken);
    }

    @Override
    public void logout(String refreshToken) {
        RefreshToken token = refreshTokenService.verify(refreshToken);
        refreshTokenService.revoke(token.getUser());
    }

    private User getUser(String name) {
        return repository.findByUsername(name)
                .or(() -> repository.findByEmail(name))
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateEmailUniqueness(String email) {
        if (repository.existsByEmail(email)) {
            throw new DuplicateResourceException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    private void validateUsernameUniqueness(String username) {
        if (repository.existsByUsername(username)) {
            throw new DuplicateResourceException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
    }
}

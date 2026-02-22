package com.example.ecommerce_clean.modules.user.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce_clean.common.exception.domain.InvalidOperationException;
import com.example.ecommerce_clean.common.exception.security.UnauthorizedException;
import com.example.ecommerce_clean.modules.user.domain.entity.RefreshToken;
import com.example.ecommerce_clean.modules.user.domain.entity.User;
import com.example.ecommerce_clean.modules.user.domain.repository.RefreshTokenRepository;
import com.example.ecommerce_clean.shared.enums.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final long REFRESH_TOKEN_DAY = 7;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public RefreshToken create(User user) {
        refreshTokenRepository.deleteByUser(user);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusDays(REFRESH_TOKEN_DAY));
        return refreshTokenRepository.save(token);
    }

    @Override
    public RefreshToken verify(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_TOKEN));

        if (refreshToken.isRevoked()) {
            throw new InvalidOperationException(ErrorCode.REFRESH_TOKEN_REVOKED);
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidOperationException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        return refreshToken;
    }

    @Override
    @Transactional
    public void revoke(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}

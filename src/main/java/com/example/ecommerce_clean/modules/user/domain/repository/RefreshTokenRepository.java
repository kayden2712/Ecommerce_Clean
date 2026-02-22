package com.example.ecommerce_clean.modules.user.domain.repository;

import java.util.Optional;

import com.example.ecommerce_clean.modules.user.domain.entity.RefreshToken;
import com.example.ecommerce_clean.modules.user.domain.entity.User;

public interface RefreshTokenRepository {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken save(RefreshToken refreshToken);

    void deleteByUser(User user);
}

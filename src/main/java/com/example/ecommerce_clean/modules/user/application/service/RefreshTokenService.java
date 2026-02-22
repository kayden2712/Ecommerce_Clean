package com.example.ecommerce_clean.modules.user.application.service;

import com.example.ecommerce_clean.modules.user.domain.entity.RefreshToken;
import com.example.ecommerce_clean.modules.user.domain.entity.User;

public interface RefreshTokenService {

    RefreshToken create(User user);

    RefreshToken verify(String token);

    void revoke(User user);
}

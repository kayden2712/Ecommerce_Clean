package com.example.ecommerce_clean.modules.user.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce_clean.modules.user.domain.entity.RefreshToken;
import com.example.ecommerce_clean.modules.user.domain.entity.User;
import com.example.ecommerce_clean.modules.user.domain.repository.RefreshTokenRepository;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.entity.UserJpaEntity;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.mapper.RefreshTokenPersistenceMapper;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.repository.RefreshTokenJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;
    private final RefreshTokenPersistenceMapper mapper;
    private final UserPersistenceMapper userMapper;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRepository.findByToken(token).map(mapper::toDomain);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return mapper.toDomain(jpaRepository.save(mapper.toJpaEntity(refreshToken)));
    }

    @Override
    @Transactional
    public void deleteByUser(User user) {
        UserJpaEntity userJpa = userMapper.toJpaEntity(user);
        jpaRepository.deleteByUser(userJpa);
    }
}

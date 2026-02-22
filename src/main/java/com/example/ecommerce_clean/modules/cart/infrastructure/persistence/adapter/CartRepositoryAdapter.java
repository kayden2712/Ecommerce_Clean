package com.example.ecommerce_clean.modules.cart.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.cart.domain.entity.Cart;
import com.example.ecommerce_clean.modules.cart.domain.repository.CartRepository;
import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.entity.CartJpaEntity;
import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.mapper.CartPersistenceMapper;
import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.repository.CartJpaRepository;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.entity.UserJpaEntity;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartRepositoryAdapter implements CartRepository {

    private final CartJpaRepository jpaRepository;
    private final CartPersistenceMapper mapper;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<Cart> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Cart> findByUserId(Long userId) {
        return userJpaRepository.findById(userId)
                .flatMap(jpaRepository::findByUser)
                .map(mapper::toDomain);
    }

    @Override
    public Cart save(Cart cart) {
        CartJpaEntity entity = mapper.toJpaEntity(cart);
        if (entity.getUser() == null && cart.getUserId() != null) {
            UserJpaEntity user = userJpaRepository.findById(cart.getUserId()).orElse(null);
            entity.setUser(user);
        }
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void delete(Cart cart) {
        jpaRepository.deleteById(cart.getId());
    }
}

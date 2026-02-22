package com.example.ecommerce_clean.modules.cart.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce_clean.modules.cart.domain.entity.CartItem;
import com.example.ecommerce_clean.modules.cart.domain.repository.CartItemRepository;
import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.entity.CartItemJpaEntity;
import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.entity.CartJpaEntity;
import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.mapper.CartItemPersistenceMapper;
import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.repository.CartItemJpaRepository;
import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.repository.CartJpaRepository;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.entity.ProductJpaEntity;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.repository.ProductJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartItemRepositoryAdapter implements CartItemRepository {

    private final CartItemJpaRepository jpaRepository;
    private final CartItemPersistenceMapper mapper;
    private final CartJpaRepository cartJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    @Override
    public List<CartItem> findByCartId(Long cartId) {
        return mapper.toDomainList(jpaRepository.findByCartId(cartId));
    }

    @Override
    public Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId) {
        return jpaRepository.findByCartIdAndProductId(cartId, productId)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteAllByCartId(Long cartId) {
        jpaRepository.deleteAllByCartId(cartId);
    }

    @Override
    public List<CartItem> findAllByCartId(Long cartId) {
        return mapper.toDomainList(jpaRepository.findAllByCartId(cartId));
    }

    @Override
    public CartItem save(CartItem cartItem) {
        CartItemJpaEntity entity;

        if (cartItem.getId() != null) {
            entity = jpaRepository.findById(cartItem.getId()).orElse(new CartItemJpaEntity());
        } else {
            entity = new CartItemJpaEntity();
        }

        if (cartItem.getCartId() != null) {
            CartJpaEntity cart = cartJpaRepository.findById(cartItem.getCartId()).orElse(null);
            entity.setCart(cart);
        }
        if (cartItem.getProductId() != null) {
            ProductJpaEntity product = productJpaRepository.findById(cartItem.getProductId()).orElse(null);
            entity.setProduct(product);
        }
        entity.setQuantity(cartItem.getQuantity());
        entity.setSelectedColor(cartItem.getSelectedColor());

        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void delete(CartItem cartItem) {
        jpaRepository.deleteById(cartItem.getId());
    }
}

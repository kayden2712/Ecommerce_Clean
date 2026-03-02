package com.example.ecommerce_clean.modules.cart.infrastructure.persistence.mapper;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.cart.domain.entity.Cart;
import com.example.ecommerce_clean.modules.cart.domain.entity.CartItem;
import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.entity.CartJpaEntity;
import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.entity.CartItemJpaEntity;
import com.example.ecommerce_clean.modules.product.infrastructure.persistence.repository.ProductJpaRepository;

import lombok.RequiredArgsConstructor;

/**
//  Mapper for converting between Cart domain entities and JPA entities
 */
@Component
@RequiredArgsConstructor
public class CartPersistenceMapper {
    
    private final CartItemPersistenceMapper cartItemMapper;
    private final ProductJpaRepository productJpaRepository;

    //  Convert CartJpaEntity to Cart domain entity with items
    public Cart toDomain(CartJpaEntity entity, List<CartItemJpaEntity> itemEntities) {
        if (entity == null) return null;
        
        List<CartItem> items = itemEntities != null 
            ? itemEntities.stream()
                .map(cartItemMapper::toDomain)
                .toList()
            : Collections.emptyList();
        
        return Cart.reconstitute(
                entity.getId(),
                entity.getUser().getId(),
                items,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
    
    //  Convert CartJpaEntity to Cart domain entity without items
    public Cart toDomain(CartJpaEntity entity) {
        return toDomain(entity, entity != null ? entity.getItems() : Collections.emptyList());
    }
    
    //  Convert Cart domain entity to JPA entity
    public CartJpaEntity toJpaEntity(Cart cart, CartJpaEntity existingEntity) {
        if (cart == null) return null;
        
        CartJpaEntity entity = existingEntity != null ? existingEntity : new CartJpaEntity();
        entity.setId(cart.getId());
        // User will be set in repository adapter
        
        // Clear and add items
        entity.getItems().clear();
        for (CartItem item : cart.getItems()) {
            CartItemJpaEntity itemEntity = new CartItemJpaEntity();
            itemEntity.setId(item.getId());
            itemEntity.setCart(entity);
            itemEntity.setProduct(productJpaRepository.findById(item.getProductId()).orElse(null));
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setSelectedColor(item.getSelectedColor());
            entity.getItems().add(itemEntity);
        }
        
        return entity;
    }
    
    //  Convert Cart domain entity to new JPA entity
    public CartJpaEntity toJpaEntity(Cart cart) {
        return toJpaEntity(cart, null);
    }
}

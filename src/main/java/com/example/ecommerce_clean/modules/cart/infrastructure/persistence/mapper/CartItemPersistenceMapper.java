package com.example.ecommerce_clean.modules.cart.infrastructure.persistence.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ecommerce_clean.modules.cart.domain.entity.CartItem;
import com.example.ecommerce_clean.modules.cart.infrastructure.persistence.entity.CartItemJpaEntity;

@Mapper(componentModel = "spring")
public interface CartItemPersistenceMapper {

    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "productPrice")
    @Mapping(source = "product.color", target = "productColor")
    @Mapping(source = "product.stock", target = "productStock")
    CartItem toDomain(CartItemJpaEntity entity);

    List<CartItem> toDomainList(List<CartItemJpaEntity> entities);
}

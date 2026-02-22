package com.example.ecommerce_clean.modules.cart.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ecommerce_clean.modules.cart.application.dto.CartItemResponse;
import com.example.ecommerce_clean.modules.cart.domain.entity.CartItem;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "productPrice", target = "price")
    @Mapping(source = "selectedColor", target = "selectedColor")
    @Mapping(source = "productColor", target = "availableColor")
    CartItemResponse toResponse(CartItem cartItem);

    List<CartItemResponse> toResponseList(List<CartItem> cartItems);
}

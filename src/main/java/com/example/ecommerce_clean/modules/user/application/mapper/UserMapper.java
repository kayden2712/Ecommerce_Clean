package com.example.ecommerce_clean.modules.user.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ecommerce_clean.modules.user.application.dto.UserResponse;
import com.example.ecommerce_clean.modules.user.domain.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole().name())")
    UserResponse toResponse(User user);
}

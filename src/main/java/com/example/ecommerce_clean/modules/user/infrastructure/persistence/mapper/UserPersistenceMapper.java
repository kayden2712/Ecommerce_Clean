package com.example.ecommerce_clean.modules.user.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.example.ecommerce_clean.modules.user.domain.entity.User;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.entity.UserJpaEntity;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    User toDomain(UserJpaEntity entity);

    UserJpaEntity toJpaEntity(User user);
}

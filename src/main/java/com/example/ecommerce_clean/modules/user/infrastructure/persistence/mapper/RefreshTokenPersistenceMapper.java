package com.example.ecommerce_clean.modules.user.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ecommerce_clean.modules.user.domain.entity.RefreshToken;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.entity.RefreshTokenJpaEntity;

@Mapper(componentModel = "spring", uses = UserPersistenceMapper.class)
public interface RefreshTokenPersistenceMapper {

    @Mapping(source = "revoked", target = "revoked")
    RefreshToken toDomain(RefreshTokenJpaEntity entity);

    @Mapping(source = "revoked", target = "revoked")
    RefreshTokenJpaEntity toJpaEntity(RefreshToken refreshToken);
}

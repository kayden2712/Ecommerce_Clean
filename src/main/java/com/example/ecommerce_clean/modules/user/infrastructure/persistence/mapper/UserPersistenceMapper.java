package com.example.ecommerce_clean.modules.user.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.user.domain.entity.User;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.entity.UserJpaEntity;

/**
 * Mapper for converting between User domain entities and JPA entities
 */
@Component
public class UserPersistenceMapper {

    /**
     * Convert UserJpaEntity to User domain entity
     */
    public User toDomain(UserJpaEntity entity) {
        if (entity == null) return null;
        
        return User.reconstitute(
                entity.getId(),
                entity.getEmail(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getFullName(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
    
    /**
     * Convert User domain entity to JPA entity
     */
    public UserJpaEntity toJpaEntity(User user) {
        if (user == null) return null;
        
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setFullName(user.getFullName());
        entity.setRole(user.getRole());
        return entity;
    }
}

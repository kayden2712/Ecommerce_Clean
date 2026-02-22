package com.example.ecommerce_clean.modules.user.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_clean.modules.user.infrastructure.persistence.entity.RefreshTokenJpaEntity;
import com.example.ecommerce_clean.modules.user.infrastructure.persistence.entity.UserJpaEntity;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {

    Optional<RefreshTokenJpaEntity> findByToken(String token);

    @Modifying
    @Query("DELETE FROM RefreshTokenJpaEntity rt WHERE rt.user = :user")
    void deleteByUser(@Param("user") UserJpaEntity user);
}

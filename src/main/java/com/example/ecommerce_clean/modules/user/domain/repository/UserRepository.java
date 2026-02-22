package com.example.ecommerce_clean.modules.user.domain.repository;

import java.util.Optional;

import com.example.ecommerce_clean.modules.user.domain.entity.User;

public interface UserRepository {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User save(User user);

    void delete(User user);
}

package com.example.ecommerce_clean.modules.user.application.service;

import com.example.ecommerce_clean.modules.user.application.dto.UserResponse;
import com.example.ecommerce_clean.modules.user.domain.entity.User;

public interface UserService {

    UserResponse getById(Long id);

    void changePassword(Long id, String newPassword);

    void changeEmail(Long id, String email);

    User findByUsername(String username);
}

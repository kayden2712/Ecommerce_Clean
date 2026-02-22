package com.example.ecommerce_clean.modules.user.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecommerce_clean.common.exception.domain.DuplicateResourceException;
import com.example.ecommerce_clean.common.exception.domain.ResourceNotFoundException;
import com.example.ecommerce_clean.modules.user.application.dto.UserResponse;
import com.example.ecommerce_clean.modules.user.application.mapper.UserMapper;
import com.example.ecommerce_clean.modules.user.domain.entity.User;
import com.example.ecommerce_clean.modules.user.domain.repository.UserRepository;
import com.example.ecommerce_clean.shared.enums.ErrorCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    @Override
    public UserResponse getById(Long id) {
        return userMapper.toResponse(getUserEntity(id));
    }

    @Override
    public void changePassword(Long id, String newPass) {
        User user = getUserEntity(id);
        user.setPassword(encoder.encode(newPass));
        repository.save(user);
    }

    @Override
    public void changeEmail(Long id, String newEmail) {
        User user = getUserEntity(id);
        validateEmail(newEmail);
        user.setEmail(newEmail);
        repository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private User getUserEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateEmail(String email) {
        if (repository.existsByEmail(email)) {
            throw new DuplicateResourceException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }
}

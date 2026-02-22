package com.example.ecommerce_clean.modules.user.domain.entity;

import java.time.LocalDateTime;

import com.example.ecommerce_clean.shared.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String email;
    private String username;
    private String password;
    private String fullName;
    private Role role;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Soft delete fields
    private boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;
}

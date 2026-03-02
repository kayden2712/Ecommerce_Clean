package com.example.ecommerce_clean.modules.user.domain.entity;

import java.time.LocalDateTime;

import com.example.ecommerce_clean.common.exception.domain.InvalidOperationException;
import com.example.ecommerce_clean.shared.enums.ErrorCode;
import com.example.ecommerce_clean.shared.enums.Role;

import lombok.Getter;

/**
 * User - Aggregate Root in DDD
 * Represents a user in the system with business logic
 */
@Getter
public class User {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MIN_USERNAME_LENGTH = 3;

    private Long id;
    private String email;
    private final String username;
    private String password; // BCrypt hashed, mutable for password changes
    private String fullName; // Mutable for profile updates
    private Role role;

    // Audit fields
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Soft delete fields
    private boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;

    // Private constructor for domain control
    private User(Long id, String email, String username, String password, String fullName, Role role,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateInputs(email, username, password);
        
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role != null ? role : Role.USER;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Factory method to create a new user (registration)
     * Password must be already hashed
     */
    public static User create(String email, String username, String hashedPassword, String fullName) {
        LocalDateTime now = LocalDateTime.now();
        return new User(null, email, username, hashedPassword, fullName, Role.USER, now, now);
    }
    
    /**
     * Factory method to create admin user
     * Password must be already hashed
     */
    public static User createAdmin(String email, String username, String hashedPassword, String fullName) {
        LocalDateTime now = LocalDateTime.now();
        return new User(null, email, username, hashedPassword, fullName, Role.ADMIN, now, now);
    }

    /**
     * Factory method to reconstitute user from persistence
     */
    public static User reconstitute(Long id, String email, String username, String password,
                                   String fullName, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new User(id, email, username, password, fullName, role, createdAt, updatedAt);
    }
    
    /**
     * Validate raw password before hashing
     * Should be called by application service before hashing
     */
    public static void validateRawPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new InvalidOperationException(
                ErrorCode.INVALID_INPUT,
                "Password cannot be null or empty"
            );
        }
        if (rawPassword.length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidOperationException(
                ErrorCode.PASSWORD_TOO_SHORT,
                "Password must be at least " + MIN_PASSWORD_LENGTH + " characters"
            );
        }
    }

    /**
     * Change user password
     * New password must be already hashed
     */
    public void updatePassword(String newHashedPassword) {
        validateNotDeleted();
        
        if (newHashedPassword == null || newHashedPassword.isBlank()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        this.password = newHashedPassword;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Change user email
     */
    public void updateEmail(String newEmail) {
        validateNotDeleted();
        validateEmail(newEmail);
        
        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Update profile information
     */
    public void updateProfile(String fullName) {
        validateNotDeleted();
        
        if (fullName != null && !fullName.trim().isEmpty()) {
            this.fullName = fullName;
        }
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Update role (admin operation)
     */
    public void updateRole(Role newRole) {
        validateNotDeleted();
        
        if (newRole == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.role = newRole;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Soft delete user
     */
    public void softDelete(String deletedBy) {
        validateNotDeleted();
        
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    // Private validation methods
    
    private void validateNotDeleted() {
        if (this.deleted) {
            throw new InvalidOperationException(
                ErrorCode.INVALID_OPERATION,
                "Cannot perform operation on deleted user"
            );
        }
    }

    private void validateInputs(String email, String username, String password) {
        validateEmail(email);
        validateUsername(username);
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }
    
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.matches("^[A-Z a-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    
    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (username.length() < MIN_USERNAME_LENGTH) {
            throw new IllegalArgumentException("Username must be at least " + MIN_USERNAME_LENGTH + " characters");
        }
    }
}

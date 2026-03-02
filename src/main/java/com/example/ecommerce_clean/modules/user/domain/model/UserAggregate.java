package com.example.ecommerce_clean.modules.user.domain.model;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import com.example.ecommerce_clean.common.exception.domain.InvalidOperationException;
import com.example.ecommerce_clean.shared.enums.ErrorCode;
import com.example.ecommerce_clean.shared.enums.Role;

/**
 * Rich Domain Model for User with business logic
 * User is an Aggregate Root managing user identity and authentication
 */
public class UserAggregate {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MAX_USERNAME_LENGTH = 50;
    
    private Long id;
    private String email;
    private final String username;
    private String password; // BCrypt hashed
    private String fullName;
    private Role role;
    
    // Audit fields
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Soft delete fields
    private boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;
    
    // Private constructor - use factory methods
    private UserAggregate(String email, String username, String hashedPassword, 
                         String fullName, Role role) {
        validateEmail(email);
        validateUsername(username);
        validateFullName(fullName);
        validateRole(role);
        
        this.email = email;
        this.username = username;
        this.password = hashedPassword;
        this.fullName = fullName;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }
    
    /**
     * Factory method to create new user (for registration)
     * Password must be already hashed
     */
    public static UserAggregate register(String email, String username, 
                                        String hashedPassword, String fullName) {
        return new UserAggregate(email, username, hashedPassword, fullName, Role.USER);
    }
    
    /**
     * Factory method to create admin user
     * Password must be already hashed
     */
    public static UserAggregate createAdmin(String email, String username, 
                                           String hashedPassword, String fullName) {
        return new UserAggregate(email, username, hashedPassword, fullName, Role.ADMIN);
    }
    
    /**
     * Reconstitute aggregate from persisted data
     * Used by mapper to rebuild domain model from database entity
     */
    public static UserAggregate reconstitute(
        Long id,
        String email,
        String username,
        String password,
        String fullName,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean deleted,
        LocalDateTime deletedAt,
        String deletedBy
    ) {
        UserAggregate aggregate = new UserAggregate(email, username, password, fullName, role);
        aggregate.id = id;
        aggregate.updatedAt = updatedAt;
        aggregate.deleted = deleted;
        aggregate.deletedAt = deletedAt;
        aggregate.deletedBy = deletedBy;
        return aggregate;
    }
    
    // Public setter for persistence layer
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Change user password
     * New password must be already hashed
     */
    public void changePassword(String newHashedPassword) {
        validateNotDeleted();
        
        if (newHashedPassword == null || newHashedPassword.isBlank()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        
        this.password = newHashedPassword;
        this.updatedAt = LocalDateTime.now();
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
     * Change user email
     */
    public void changeEmail(String newEmail) {
        validateNotDeleted();
        validateEmail(newEmail);
        
        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Update user profile information
     */
    public void updateProfile(String fullName) {
        validateNotDeleted();
        validateFullName(fullName);
        
        this.fullName = fullName;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Promote user to admin
     */
    public void promoteToAdmin() {
        validateNotDeleted();
        
        if (this.role == Role.ADMIN) {
            throw new InvalidOperationException(
                ErrorCode.INVALID_OPERATION,
                "User is already an admin"
            );
        }
        
        this.role = Role.ADMIN;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Demote admin to regular user
     */
    public void demoteToUser() {
        validateNotDeleted();
        
        if (this.role == Role.USER) {
            throw new InvalidOperationException(
                ErrorCode.INVALID_OPERATION,
                "User is already a regular user"
            );
        }
        
        this.role = Role.USER;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Check if user is admin
     */
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
    
    /**
     * Check if user is regular user
     */
    public boolean isRegularUser() {
        return this.role == Role.USER;
    }
    
    /**
     * Soft delete user
     */
    public void softDelete(String deletedBy) {
        if (this.deleted) {
            throw new InvalidOperationException(
                ErrorCode.INVALID_OPERATION,
                "User is already deleted"
            );
        }
        
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Restore soft-deleted user
     */
    public void restore() {
        if (!this.deleted) {
            throw new InvalidOperationException(
                ErrorCode.INVALID_OPERATION,
                "User is not deleted"
            );
        }
        
        this.deleted = false;
        this.deletedAt = null;
        this.deletedBy = null;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Private validation methods
    
    private void validateNotDeleted() {
        if (this.deleted) {
            throw new InvalidOperationException(
                ErrorCode.INVALID_OPERATION,
                "Cannot modify deleted user"
            );
        }
    }
    
    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidOperationException(
                ErrorCode.INVALID_INPUT,
                "Invalid email format"
            );
        }
    }
    
    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (username.length() < MIN_USERNAME_LENGTH) {
            throw new InvalidOperationException(
                ErrorCode.INVALID_INPUT,
                "Username must be at least " + MIN_USERNAME_LENGTH + " characters"
            );
        }
        if (username.length() > MAX_USERNAME_LENGTH) {
            throw new InvalidOperationException(
                ErrorCode.INVALID_INPUT,
                "Username cannot exceed " + MAX_USERNAME_LENGTH + " characters"
            );
        }
        // Username can only contain letters, numbers, underscores, and hyphens
        if (!username.matches("^[a-zA-Z0-9_-]+$")) {
            throw new InvalidOperationException(
                ErrorCode.INVALID_INPUT,
                "Username can only contain letters, numbers, underscores, and hyphens"
            );
        }
    }
    
    private void validateFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        if (fullName.length() > 100) {
            throw new InvalidOperationException(
                ErrorCode.INVALID_INPUT,
                "Full name cannot exceed 100 characters"
            );
        }
    }
    
    private void validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
    }
    
    // Getters (no setters to maintain encapsulation)
    
    public Long getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public Role getRole() {
        return role;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public boolean isDeleted() {
        return deleted;
    }
    
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
    
    public String getDeletedBy() {
        return deletedBy;
    }
}

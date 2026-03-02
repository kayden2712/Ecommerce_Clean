package com.example.ecommerce_clean.modules.chatbot.domain.model;

import java.time.LocalDateTime;

import com.example.ecommerce_clean.shared.enums.MessageType;

/**
 * Value Object representing a chat message
 * Immutable - once created, cannot be modified
 */
public record ChatMessage(
    Long id,
    String content,
    MessageType messageType,
    LocalDateTime createAt
) {
    
    // Compact constructor with validation
    public ChatMessage {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }
        if (messageType == null) {
            throw new IllegalArgumentException("Message type cannot be null");
        }
        if (createAt == null) {
            createAt = LocalDateTime.now();
        }
    }
    
    // Factory methods for better semantics
    public static ChatMessage userMessage(String content) {
        return new ChatMessage(null, content, MessageType.USER, LocalDateTime.now());
    }
    
    public static ChatMessage botMessage(String content) {
        return new ChatMessage(null, content, MessageType.CHATBOT, LocalDateTime.now());
    }
    
    public boolean isFromUser() {
        return messageType == MessageType.USER;
    }
    
    public boolean isFromBot() {
        return messageType == MessageType.CHATBOT;
    }
}

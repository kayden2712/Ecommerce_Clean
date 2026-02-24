package com.example.ecommerce_clean.modules.chatbot.domain.model;

import java.time.LocalDateTime;

import com.example.ecommerce_clean.shared.enums.MessageType;

import lombok.Getter;

@Getter
public class ChatMessage {
    private Long id;
    private String content;
    private MessageType messageType;
    private LocalDateTime createAt;

    public ChatMessage(Long id, String content, MessageType messageType) {
        this.content = content;
        this.id = id;
        this.messageType = messageType;
        this.createAt = LocalDateTime.now();
    }

    public static ChatMessage userMessage(String content) {
        return new ChatMessage(null, content, MessageType.USER);
    }
    public static ChatMessage botMessage(String content) {
        return new ChatMessage(null, content, MessageType.CHATBOT);
    }
}

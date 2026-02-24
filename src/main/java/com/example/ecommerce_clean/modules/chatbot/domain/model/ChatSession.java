package com.example.ecommerce_clean.modules.chatbot.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.example.ecommerce_clean.shared.enums.SessionStatus;

import lombok.Getter;

@Getter
public class ChatSession {
    private Long id;
    private Long userId;
    private List<ChatMessage> messages = new ArrayList<>();
    private SessionStatus status = SessionStatus.ACTIVE;

    public ChatSession(Long userId) {
        this.userId = userId;
    }

    public void addUserMessage(String content) {
        validateActive();
        messages.add(ChatMessage.userMessage(content));
    }

    public void addBotMessage(String content) {
        messages.add(ChatMessage.botMessage(content));
    }

    public void close() {
        this.status = SessionStatus.CLOSED;
    }

    private void validateActive() {
        if (status == SessionStatus.CLOSED) {
            throw new IllegalStateException("Chat session is closed");
        }
    }
}

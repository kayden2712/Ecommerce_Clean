package com.example.ecommerce_clean.modules.chatbot.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.ecommerce_clean.shared.enums.SessionStatus;

class ChatSessionTest {

    private ChatSession session;

    @BeforeEach
    void setUp() {
        session = new ChatSession(1L);
    }

    @Test
    @DisplayName("Tạo session mới → status ACTIVE, messages rỗng")
    void shouldCreateNewSessionWithActiveStatus() {
        assertEquals(1L, session.getUserId());
        assertEquals(SessionStatus.ACTIVE, session.getStatus());
        assertTrue(session.getMessages().isEmpty());
    }

    @Test
    @DisplayName("Thêm user message → messages có 1 phần tử")
    void shouldAddUserMessage() {
        session.addUserMessage("Xin chào");

        assertEquals(1, session.getMessages().size());
    }

    @Test
    @DisplayName("Thêm bot message → messages có 1 phần tử")
    void shouldAddBotMessage() {
        session.addBotMessage("Chào bạn!");

        assertEquals(1, session.getMessages().size());
    }

    @Test
    @DisplayName("Close session → status CLOSED")
    void shouldCloseSession() {
        session.close();

        assertEquals(SessionStatus.CLOSED, session.getStatus());
    }

    @Test
    @DisplayName("Thêm user message vào session đã close → throw IllegalStateException")
    void shouldThrowWhenAddingMessageToClosedSession() {
        session.close();

        assertThrows(IllegalStateException.class, () -> session.addUserMessage("test"));
    }
}

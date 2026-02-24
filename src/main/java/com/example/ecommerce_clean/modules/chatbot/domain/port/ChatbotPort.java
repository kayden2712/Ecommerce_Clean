package com.example.ecommerce_clean.modules.chatbot.domain.port;

import java.util.List;

import com.example.ecommerce_clean.modules.chatbot.domain.model.ChatMessage;

public interface ChatbotPort {

    /**
     * Send a conversation to AI with a system prompt for context.
     *
     * @param systemPrompt instructions/context for the AI (e.g., ecommerce data)
     * @param history      conversation history
     * @return AI-generated response
     */
    String chat(String systemPrompt, List<ChatMessage> history);
}

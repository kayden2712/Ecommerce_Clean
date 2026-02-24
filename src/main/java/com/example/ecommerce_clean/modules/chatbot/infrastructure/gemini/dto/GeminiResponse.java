package com.example.ecommerce_clean.modules.chatbot.infrastructure.gemini.dto;

import java.util.List;

/**
 * DTO matching Gemini API response format.
 * <pre>
 * {
 *   "candidates": [
 *     {
 *       "content": {
 *         "parts": [{"text": "response text"}],
 *         "role": "model"
 *       }
 *     }
 *   ]
 * }
 * </pre>
 */
public record GeminiResponse(List<Candidate> candidates) {

    public record Candidate(Content content) {}

    public record Content(List<Part> parts, String role) {}

    public record Part(String text) {}

    /**
     * Extracts the text reply from the first candidate.
     */
    public String extractText() {
        if (candidates == null || candidates.isEmpty()) {
            return "Xin lỗi, tôi không thể trả lời lúc này.";
        }
        var parts = candidates.getFirst().content().parts();
        if (parts == null || parts.isEmpty()) {
            return "Xin lỗi, tôi không thể trả lời lúc này.";
        }
        return parts.getFirst().text();
    }
}

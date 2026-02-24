package com.example.ecommerce_clean.modules.chatbot.infrastructure.gemini.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO matching Gemini API request format.
 * <pre>
 * {
 *   "systemInstruction": { "parts": [{"text": "..."}] },
 *   "contents": [
 *     { "role": "user",  "parts": [{"text": "Hello"}] },
 *     { "role": "model", "parts": [{"text": "Hi!"}] }
 *   ]
 * }
 * </pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GeminiRequest(
        Content systemInstruction,
        List<Content> contents
) {

    public record Content(String role, List<Part> parts) {
        public static Content of(String role, String text) {
            return new Content(role, List.of(new Part(text)));
        }
    }

    public record Part(String text) {}
}

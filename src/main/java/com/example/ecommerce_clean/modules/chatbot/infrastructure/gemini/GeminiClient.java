package com.example.ecommerce_clean.modules.chatbot.infrastructure.gemini;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.ecommerce_clean.modules.chatbot.domain.model.ChatMessage;
import com.example.ecommerce_clean.modules.chatbot.domain.port.ChatbotPort;
import com.example.ecommerce_clean.modules.chatbot.infrastructure.gemini.dto.GeminiRequest;
import com.example.ecommerce_clean.modules.chatbot.infrastructure.gemini.dto.GeminiRequest.Content;
import com.example.ecommerce_clean.modules.chatbot.infrastructure.gemini.dto.GeminiResponse;
import com.example.ecommerce_clean.shared.enums.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient implements ChatbotPort {

    private final WebClient geminiWebClient;

    @Override
    public String chat(String systemPrompt, List<ChatMessage> history) {
        GeminiRequest request = buildRequest(systemPrompt, history);

        try {
            GeminiResponse response = geminiWebClient.post()
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .block();

            if (response == null) {
                return "Xin lỗi, tôi không thể trả lời lúc này.";
            }
            return response.extractText();

        } catch (WebClientResponseException e) {
            log.error("Gemini API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode().value() == 429) {
                return "Hệ thống AI đang quá tải (rate limit). Vui lòng thử lại sau ít phút.";
            }
            return "Xin lỗi, hệ thống AI đang gặp sự cố. Vui lòng thử lại sau.";
        } catch (Exception e) {
            log.error("Unexpected error calling Gemini API", e);
            return "Xin lỗi, đã xảy ra lỗi. Vui lòng thử lại sau.";
        }
    }

    private GeminiRequest buildRequest(String systemPrompt, List<ChatMessage> history) {
        Content systemInstruction = Content.of(null, systemPrompt);

        List<Content> contents = history.stream()
                .map(msg -> Content.of(
                        msg.getMessageType() == MessageType.USER ? "user" : "model",
                        msg.getContent()
                ))
                .toList();

        return new GeminiRequest(systemInstruction, contents);
    }
}
    
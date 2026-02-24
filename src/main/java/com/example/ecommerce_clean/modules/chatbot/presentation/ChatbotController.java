package com.example.ecommerce_clean.modules.chatbot.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_clean.modules.chatbot.application.dto.ChatbotRequest;
import com.example.ecommerce_clean.modules.chatbot.application.dto.ChatbotResponse;
import com.example.ecommerce_clean.modules.chatbot.application.service.ChatbotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/chat")
    public ResponseEntity<ChatbotResponse> chat(@RequestBody ChatbotRequest request) {
        ChatbotResponse response = chatbotService.handle(request);
        return ResponseEntity.ok(response);
    }
}

package com.example.ecommerce_clean.modules.chatbot.application.service;

import org.springframework.stereotype.Service;

import com.example.ecommerce_clean.modules.chatbot.application.dto.ChatbotRequest;
import com.example.ecommerce_clean.modules.chatbot.application.dto.ChatbotResponse;
import com.example.ecommerce_clean.modules.chatbot.application.intent.IntentDetector;
import com.example.ecommerce_clean.modules.chatbot.domain.model.ChatSession;
import com.example.ecommerce_clean.modules.chatbot.domain.port.ChatbotPort;
import com.example.ecommerce_clean.modules.chatbot.domain.port.OrderQueryPort;
import com.example.ecommerce_clean.modules.chatbot.domain.port.PaymentQueryPort;
import com.example.ecommerce_clean.modules.chatbot.domain.port.ProductQueryPort;
import com.example.ecommerce_clean.shared.enums.IntentType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private static final String BASE_SYSTEM_PROMPT = """
            Bạn là trợ lý AI của một cửa hàng thương mại điện tử.
            Hãy trả lời ngắn gọn, thân thiện và hữu ích.
            Nếu được cung cấp dữ liệu hệ thống, hãy dựa vào đó để trả lời.
            Trả lời bằng tiếng Việt trừ khi người dùng dùng tiếng Anh.
            """;

    private final ChatbotPort chatbotPort;
    private final ProductQueryPort productQueryPort;
    private final OrderQueryPort orderQueryPort;
    private final PaymentQueryPort paymentQueryPort;

    public ChatbotResponse handle(ChatbotRequest request) {
        ChatSession session = new ChatSession(request.userId());
        session.addUserMessage(request.message());

        IntentType intent = IntentDetector.detect(request.message());
        String context = fetchContext(intent, request);
        String systemPrompt = buildSystemPrompt(context);

        String reply = chatbotPort.chat(systemPrompt, session.getMessages());

        session.addBotMessage(reply);
        return new ChatbotResponse(session.getId(), reply);
    }

    private String fetchContext(IntentType intent, ChatbotRequest request) {
        return switch (intent) {
            case PRODUCT_QUERY -> productQueryPort.getProductInfo(request.message());
            case ORDER_QUERY -> orderQueryPort.getOrderInfo(request.userId(), request.message());
            case PAYMENT_QUERY -> paymentQueryPort.getPaymentStatus(request.userId(), request.message());
            case GENERAL_CHAT -> null;
        };
    }

    private String buildSystemPrompt(String context) {
        if (context == null || context.isBlank()) {
            return BASE_SYSTEM_PROMPT;
        }
        return BASE_SYSTEM_PROMPT + "\n\nDữ liệu hệ thống:\n" + context;
    }
}

 package com.example.ecommerce_clean.modules.chatbot.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ecommerce_clean.modules.chatbot.application.dto.ChatbotRequest;
import com.example.ecommerce_clean.modules.chatbot.application.dto.ChatbotResponse;
import com.example.ecommerce_clean.modules.chatbot.domain.port.ChatbotPort;
import com.example.ecommerce_clean.modules.chatbot.domain.port.OrderQueryPort;
import com.example.ecommerce_clean.modules.chatbot.domain.port.PaymentQueryPort;
import com.example.ecommerce_clean.modules.chatbot.domain.port.ProductQueryPort;

@ExtendWith(MockitoExtension.class)
class ChatbotServiceTest {

    @Mock
    private ChatbotPort chatbotPort;

    @Mock
    private ProductQueryPort productQueryPort;

    @Mock
    private OrderQueryPort orderQueryPort;

    @Mock
    private PaymentQueryPort paymentQueryPort;

    @InjectMocks
    private ChatbotService chatbotService;

    @Nested
    @DisplayName("handle() - General Chat")
    class GeneralChatTests {

        @Test
        @DisplayName("Khi message là câu chào → không gọi query port nào, trả về reply từ AI")
        void shouldHandleGeneralChat() {
            // Given
            ChatbotRequest request = new ChatbotRequest(null, 1L, "Xin chào!");
            when(chatbotPort.chat(anyString(), anyList())).thenReturn("Chào bạn! Tôi có thể giúp gì?");

            // When
            ChatbotResponse response = chatbotService.handle(request);

            // Then
            assertNotNull(response);
            assertEquals("Chào bạn! Tôi có thể giúp gì?", response.reply());

            // Verify không gọi bất kỳ query port nào
            verifyNoInteractions(productQueryPort);
            verifyNoInteractions(orderQueryPort);
            verifyNoInteractions(paymentQueryPort);

            // Verify chatbotPort.chat() được gọi đúng 1 lần
            verify(chatbotPort, times(1)).chat(anyString(), anyList());
        }
    }

    @Nested
    @DisplayName("handle() - Product Query")
    class ProductQueryTests {

        @Test
        @DisplayName("Khi message chứa từ khóa 'sản phẩm' → gọi productQueryPort")
        void shouldQueryProductWhenMessageContainsProductKeyword() {
            // Given
            ChatbotRequest request = new ChatbotRequest(null, 1L, "Tôi muốn xem sản phẩm iPhone");
            when(productQueryPort.getProductInfo(anyString())).thenReturn("iPhone 15 - Giá: 25.000.000đ");
            when(chatbotPort.chat(anyString(), anyList())).thenReturn("Dạ, iPhone 15 có giá 25 triệu ạ!");

            // When
            ChatbotResponse response = chatbotService.handle(request);

            // Then
            assertNotNull(response);
            assertEquals("Dạ, iPhone 15 có giá 25 triệu ạ!", response.reply());

            // Verify productQueryPort được gọi
            verify(productQueryPort, times(1)).getProductInfo("Tôi muốn xem sản phẩm iPhone");

            // Verify các port khác KHÔNG được gọi
            verifyNoInteractions(orderQueryPort);
            verifyNoInteractions(paymentQueryPort);
        }

        @Test
        @DisplayName("Khi message chứa từ khóa 'giá' → gọi productQueryPort")
        void shouldQueryProductWhenMessageContainsPriceKeyword() {
            // Given
            ChatbotRequest request = new ChatbotRequest(null, 1L, "giá laptop bao nhiêu?");
            when(productQueryPort.getProductInfo(anyString())).thenReturn("Laptop ABC - 15.000.000đ");
            when(chatbotPort.chat(anyString(), anyList())).thenReturn("Laptop ABC giá 15 triệu ạ!");

            // When
            ChatbotResponse response = chatbotService.handle(request);

            // Then
            assertEquals("Laptop ABC giá 15 triệu ạ!", response.reply());
            verify(productQueryPort).getProductInfo("giá laptop bao nhiêu?");
        }
    }

    @Nested
    @DisplayName("handle() - Order Query")
    class OrderQueryTests {

        @Test
        @DisplayName("Khi message chứa từ khóa 'đơn hàng' → gọi orderQueryPort")
        void shouldQueryOrderWhenMessageContainsOrderKeyword() {
            // Given
            ChatbotRequest request = new ChatbotRequest(null, 1L, "Kiểm tra đơn hàng của tôi");
            when(orderQueryPort.getOrderInfo(eq(1L), anyString())).thenReturn("Đơn #123 - Đang giao");
            when(chatbotPort.chat(anyString(), anyList())).thenReturn("Đơn hàng #123 đang được giao.");

            // When
            ChatbotResponse response = chatbotService.handle(request);

            // Then
            assertEquals("Đơn hàng #123 đang được giao.", response.reply());
            verify(orderQueryPort).getOrderInfo(1L, "Kiểm tra đơn hàng của tôi");
            verifyNoInteractions(productQueryPort);
            verifyNoInteractions(paymentQueryPort);
        }
    }

    @Nested
    @DisplayName("handle() - Payment Query")
    class PaymentQueryTests {

        @Test
        @DisplayName("Khi message chứa từ khóa 'thanh toán' → gọi paymentQueryPort")
        void shouldQueryPaymentWhenMessageContainsPaymentKeyword() {
            // Given
            ChatbotRequest request = new ChatbotRequest(null, 1L, "Trạng thái thanh toán đơn #123");
            when(paymentQueryPort.getPaymentStatus(eq(1L), anyString())).thenReturn("Đã thanh toán");
            when(chatbotPort.chat(anyString(), anyList())).thenReturn("Đơn #123 đã thanh toán thành công.");

            // When
            ChatbotResponse response = chatbotService.handle(request);

            // Then
            assertEquals("Đơn #123 đã thanh toán thành công.", response.reply());
            verify(paymentQueryPort).getPaymentStatus(1L, "Trạng thái thanh toán đơn #123");
            verifyNoInteractions(productQueryPort);
            verifyNoInteractions(orderQueryPort);
        }
    }

    @Nested
    @DisplayName("handle() - System Prompt")
    class SystemPromptTests {

        @Test
        @DisplayName("Khi có context → system prompt chứa dữ liệu hệ thống")
        void shouldIncludeContextInSystemPrompt() {
            // Given
            ChatbotRequest request = new ChatbotRequest(null, 1L, "Tôi muốn xem sản phẩm");
            when(productQueryPort.getProductInfo(anyString())).thenReturn("Sản phẩm A, B, C");
            when(chatbotPort.chat(anyString(), anyList())).thenReturn("reply");

            // When
            chatbotService.handle(request);

            // Then - capture system prompt và verify nó chứa context
            verify(chatbotPort).chat(
                    argThat(systemPrompt -> systemPrompt.contains("Dữ liệu hệ thống") 
                            && systemPrompt.contains("Sản phẩm A, B, C")),
                    anyList()
            );
        }

        @Test
        @DisplayName("Khi không có context (general chat) → system prompt không chứa dữ liệu hệ thống")
        void shouldNotIncludeContextForGeneralChat() {
            // Given
            ChatbotRequest request = new ChatbotRequest(null, 1L, "Xin chào!");
            when(chatbotPort.chat(anyString(), anyList())).thenReturn("reply");

            // When
            chatbotService.handle(request);

            // Then
            verify(chatbotPort).chat(
                    argThat(systemPrompt -> !systemPrompt.contains("Dữ liệu hệ thống")),
                    anyList()
            );
        }
    }
}

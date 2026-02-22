package com.example.ecommerce_clean.modules.order.presentation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_clean.modules.order.application.dto.OrderResponse;
import com.example.ecommerce_clean.modules.order.application.service.OrderService;
import com.example.ecommerce_clean.shared.enums.OrderStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse placeOrder(Authentication authentication) {
        return orderService.placeOrder(authentication.getName());
    }

    @DeleteMapping("/{orderId}")
    public void cancelOrder(@PathVariable Long orderId, Authentication authentication) {
        orderService.cancelOrder(orderId, authentication.getName());
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable Long orderId, Authentication authentication) {
        return orderService.getOrder(orderId, authentication.getName());
    }

    @GetMapping("/history")
    public Page<OrderResponse> getOrderHistory(
            Authentication authentication,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return orderService.getOrderHistory(authentication.getName(), status, pageable);
    }
}

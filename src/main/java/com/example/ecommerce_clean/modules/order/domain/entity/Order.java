package com.example.ecommerce_clean.modules.order.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.ecommerce_clean.common.exception.domain.InvalidOperationException;
import com.example.ecommerce_clean.modules.order.domain.model.OrderState;
import com.example.ecommerce_clean.shared.enums.ErrorCode;
import com.example.ecommerce_clean.shared.enums.OrderStatus;

import lombok.Getter;

@Getter
public class Order {

    private Long id;
    private final Long userId;
    private final String username;
    private List<OrderItem> items = new ArrayList<>();
    private BigDecimal totalPrice;
    private OrderStatus status;
    private OrderState state;

    // Audit fields
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Soft delete fields
    private boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;

    private Order(Long id, Long userId, String username, OrderStatus status,
            List<OrderItem> items, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.status = status;
        this.items = items;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.state = mapToState(status); // Tự động map trạng thái khi khởi tạo
    }

    public static Order create(Long userId, String username) {
        return new Order(null, userId, username, OrderStatus.PENDING,
                new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
    }

    public static Order reconstitute(Long id, Long userId, String username,
            OrderStatus status, List<OrderItem> items,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Order(id, userId, username, status, items, createdAt, updatedAt);
    }

    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }
        this.items.add(item);
        this.totalPrice = calculateTotalPrice();
        this.updatedAt = LocalDateTime.now();
    }

    public void addItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be null or empty");
        }
        this.items.addAll(items);
        this.totalPrice = calculateTotalPrice();
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel(String reason, String cancelledBy) {
        if (!state.canBeCancelled()) {
            throw new InvalidOperationException(ErrorCode.ORDER_CANNOT_BE_CANCELLED);
        }
        this.status = OrderStatus.CANCELLED;
        this.state = new OrderState.Cancelled(LocalDateTime.now(), reason, cancelledBy);
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal calculateTotalPrice() {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderState mapToState(OrderStatus status) {
        return switch (status) {
            case PENDING -> new OrderState.Pending(this.createdAt);

            case PAID -> new OrderState.Paid(
                    this.updatedAt,
                    "PAYMENT-" + this.id // Hoặc lấy từ một field paymentId nếu có
                );

            case SHIPPED -> new OrderState.Shipped(
                    this.updatedAt,
                    "TRACK-" + this.id, // Reconstitute nên dùng dữ liệu thật từ DB
                    "Standard Carrier");

            case COMPLETED -> new OrderState.Completed(
                    this.updatedAt,
                    this.updatedAt // Thời gian hoàn thành thường là updatedAt cuối cùng
                );

            case CANCELLED -> new OrderState.Cancelled(
                    this.updatedAt,
                    "Reason captured from history",
                    "System/User");
        };
    }
}
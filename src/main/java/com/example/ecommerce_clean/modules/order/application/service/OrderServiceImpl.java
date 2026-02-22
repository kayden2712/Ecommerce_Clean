package com.example.ecommerce_clean.modules.order.application.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce_clean.common.exception.domain.InvalidOperationException;
import com.example.ecommerce_clean.common.exception.domain.ResourceNotFoundException;
import com.example.ecommerce_clean.modules.cart.application.service.CartService;
import com.example.ecommerce_clean.modules.cart.domain.entity.Cart;
import com.example.ecommerce_clean.modules.cart.domain.entity.CartItem;
import com.example.ecommerce_clean.modules.cart.domain.repository.CartItemRepository;
import com.example.ecommerce_clean.modules.cart.domain.repository.CartRepository;
import com.example.ecommerce_clean.modules.order.application.dto.OrderResponse;
import com.example.ecommerce_clean.modules.order.application.mapper.OrderMapper;
import com.example.ecommerce_clean.modules.order.domain.entity.Order;
import com.example.ecommerce_clean.modules.order.domain.entity.OrderItem;
import com.example.ecommerce_clean.modules.order.domain.repository.OrderRepository;
import com.example.ecommerce_clean.modules.product.domain.entity.Product;
import com.example.ecommerce_clean.modules.product.domain.repository.ProductRepository;
import com.example.ecommerce_clean.modules.user.domain.entity.User;
import com.example.ecommerce_clean.modules.user.domain.repository.UserRepository;
import com.example.ecommerce_clean.shared.enums.ErrorCode;
import com.example.ecommerce_clean.shared.enums.OrderStatus;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final OrderValidator orderValidator;
    private final OrderPricingService orderPricingService;
    private final OrderMapper orderMapper;
    private final CartService cartService;

    @Override
    public OrderResponse placeOrder(String username) {
        User user = getUserByUsername(username);
        Cart cart = getCartByUserId(user.getId());
        List<CartItem> cartItems = getCartItems(cart);

        Order order = createPendingOrder(user);

        List<OrderItem> orderItems = buildOrder(order, cartItems);
        order.setItems(orderItems);

        // Pricing
        BigDecimal totalPrice = orderPricingService.calculateOrderTotal(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(username);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public void cancelOrder(Long orderId, String username) {
        Order order = getOrderById(orderId);
        if (!order.getUsername().equals(username)) {
            throw new InvalidOperationException(ErrorCode.ORDER_ACCESS_DENIED);
        }
        orderValidator.validateCancel(order);
        restoreStock(order);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId, String username) {
        Order order = getOrderById(orderId);
        if (!order.getUsername().equals(username)) {
            throw new InvalidOperationException(ErrorCode.ORDER_ACCESS_DENIED);
        }
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrderHistory(String username, OrderStatus status, Pageable pageable) {
        User user = getUserByUsername(username);
        Page<Order> orders;
        if (status != null) {
            orders = orderRepository.findByUserIdAndStatus(user.getId(), status, pageable);
        } else {
            orders = orderRepository.findByUserId(user.getId(), pageable);
        }
        return orders.map(orderMapper::toResponse);
    }

    // ── Private helpers ──

    // Fetch user by username
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    // Fetch cart by user ID
    private Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CART_NOT_FOUND));
    }

    // Fetch cart items
    private List<CartItem> getCartItems(Cart cart) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new InvalidOperationException(ErrorCode.CART_EMPTY);
        }
        return cartItems;
    }

    // Create a new order with PENDING status
    private Order createPendingOrder(User user) {
        Order order = new Order();
        order.setUserId(user.getId());
        order.setUsername(user.getUsername());
        order.setStatus(OrderStatus.PENDING);
        return order;
    }

    // Build order items from cart items
    private List<OrderItem> buildOrder(Order order, List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
                    int quantity = cartItem.getQuantity();
                    validateStock(product, quantity);
                    product.setStock(product.getStock() - quantity);
                    productRepository.save(product);

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(product.getId());
                    orderItem.setProductName(product.getName());
                    orderItem.setQuantity(quantity);
                    orderItem.setPrice(product.getPrice());
                    orderItem.setTotalPrice(orderPricingService.calculateItemTotal(orderItem));
                    return orderItem;
                })
                .toList();
    }

    // Validate stock availability
    private void validateStock(Product product, Integer quantity) {
        if (product.getStock() < quantity) {
            throw new InvalidOperationException(ErrorCode.INSUFFICIENT_STOCK);
        }
    }

    // Fetch order by ID
    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ORDER_NOT_FOUND));
    }

    // Restore stock when order is cancelled
    private void restoreStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
    }
}

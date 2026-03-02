package com.example.ecommerce_clean.modules.order.application.service;

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

    private final OrderMapper orderMapper;
    private final CartService cartService;

    @Override
    public OrderResponse placeOrder(String username) {
        User user = getUserByUsername(username);
        Cart cart = getCartByUserId(user.getId());
        List<CartItem> cartItems = getCartItems(cart);

        // Create Order using domain entity
        Order order = Order.create(user.getId(), user.getUsername());

        // Build and add order items
        List<OrderItem> orderItems = buildOrderItems(cartItems);
        order.addItems(orderItems);

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Clear cart after successful order
        cartService.clearCart(username);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public void cancelOrder(Long orderId, String username) {
        Order order = getOrderById(orderId);
        
        // Validate access rights
        if (!order.getUsername().equals(username)) {
            throw new InvalidOperationException(ErrorCode.ORDER_ACCESS_DENIED);
        }
        
        // Use domain logic to cancel (handles state validation)
        order.cancel("Cancelled by user", username);
        
        // Restore stock before persisting
        restoreStock(order);
        
        // Save the cancelled order
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

    // Build order items from cart items
    private List<OrderItem> buildOrderItems(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
                    int quantity = cartItem.getQuantity();
                    validateStock(product, quantity);
                    
                    // Create OrderItem using factory method (domain logic)
                    OrderItem orderItem = OrderItem.create(
                            product.getId(),
                            product.getName(),
                            quantity,
                            product.getPrice().amount() // Extract BigDecimal from Money
                    );
                    
                    // Update stock
                    product.setStock(product.getStock() - quantity);
                    productRepository.save(product);
                    
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

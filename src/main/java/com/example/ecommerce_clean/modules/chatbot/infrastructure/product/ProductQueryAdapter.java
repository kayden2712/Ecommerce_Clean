package com.example.ecommerce_clean.modules.chatbot.infrastructure.product;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.ecommerce_clean.modules.chatbot.domain.port.ProductQueryPort;
import com.example.ecommerce_clean.modules.product.application.dto.ProductResponse;
import com.example.ecommerce_clean.modules.product.application.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductQueryAdapter implements ProductQueryPort {

    private final ProductService productService;

    @Override
    public String getProductInfo(String message) {
        try {
            List<ProductResponse> products = productService.getAllProducts();
            if (products.isEmpty()) {
                return "Hiện tại không có sản phẩm nào trong hệ thống.";
            }

            StringBuilder sb = new StringBuilder("Danh sách sản phẩm hiện có:\n");
            for (ProductResponse p : products) {
                sb.append(String.format("- %s | Giá: %,.0f VND | Tồn kho: %d | Màu: %s | Danh mục: %s\n",
                        p.name(), p.price(), p.stock(), p.color(), p.categoryName()));
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("Error fetching product info", e);
            return "Không thể lấy thông tin sản phẩm lúc này.";
        }
    }
}

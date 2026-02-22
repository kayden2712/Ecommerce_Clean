package com.example.ecommerce_clean.modules.product.application.service;

import java.util.List;

import com.example.ecommerce_clean.modules.product.application.dto.CreateProductRequest;
import com.example.ecommerce_clean.modules.product.application.dto.ProductResponse;
import com.example.ecommerce_clean.modules.product.application.dto.UpdateProductRequest;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse updateProduct(Long productId, UpdateProductRequest request);

    void deleteProduct(Long productId);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long productId);

    void increaseProductStock(Long productId, int stock);

    void decreaseProductStock(Long productId, int stock);
}

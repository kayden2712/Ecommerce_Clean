package com.example.ecommerce_clean.modules.product.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_clean.modules.product.application.dto.CreateProductRequest;
import com.example.ecommerce_clean.modules.product.application.dto.ProductResponse;
import com.example.ecommerce_clean.modules.product.application.dto.UpdateProductRequest;
import com.example.ecommerce_clean.modules.product.application.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody CreateProductRequest request) {
        return service.createProduct(request);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest request) {
        return service.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return service.getProductById(id);
    }

    @PatchMapping("/{id}/increase-stock")
    public void increaseProductStock(@PathVariable Long id, @RequestParam int quantity) {
        service.increaseProductStock(id, quantity);
    }

    @PatchMapping("/{id}/decrease-stock")
    public void decreaseProductStock(@PathVariable Long id, @RequestParam int quantity) {
        service.decreaseProductStock(id, quantity);
    }
}

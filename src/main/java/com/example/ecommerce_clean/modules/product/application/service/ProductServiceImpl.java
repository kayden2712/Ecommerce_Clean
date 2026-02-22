package com.example.ecommerce_clean.modules.product.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce_clean.common.exception.domain.InvalidOperationException;
import com.example.ecommerce_clean.common.exception.domain.ResourceNotFoundException;
import com.example.ecommerce_clean.modules.product.application.dto.CreateProductRequest;
import com.example.ecommerce_clean.modules.product.application.dto.ProductResponse;
import com.example.ecommerce_clean.modules.product.application.dto.UpdateProductRequest;
import com.example.ecommerce_clean.modules.product.application.mapper.ProductMapper;
import com.example.ecommerce_clean.modules.product.domain.entity.Category;
import com.example.ecommerce_clean.modules.product.domain.entity.Product;
import com.example.ecommerce_clean.modules.product.domain.repository.CategoryRepository;
import com.example.ecommerce_clean.modules.product.domain.repository.ProductRepository;
import com.example.ecommerce_clean.shared.enums.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Category category = getCategoryEntityById(request.categoryId());
        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        return productMapper.toResponse(repository.save(product));
    }

    @Override
    public ProductResponse updateProduct(Long productId, UpdateProductRequest request) {
        Product product = getProductEntityById(productId);
        productMapper.updateEntity(request, product);
        Category category = getCategoryEntityById(request.categoryId());
        product.setCategory(category);
        return productMapper.toResponse(repository.save(product));
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = getProductEntityById(productId);
        repository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return repository.findAll().stream().map(productMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        return productMapper.toResponse(getProductEntityById(productId));
    }

    @Override
    public void increaseProductStock(Long productId, int stock) {
        Product product = getProductEntityById(productId);
        product.setStock(product.getStock() + stock);
        repository.save(product);
    }

    @Override
    public void decreaseProductStock(Long productId, int stock) {
        Product product = getProductEntityById(productId);
        if (product.getStock() < stock) {
            throw new InvalidOperationException(ErrorCode.INSUFFICIENT_STOCK);
        }
        product.setStock(product.getStock() - stock);
        repository.save(product);
    }

    private Category getCategoryEntityById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private Product getProductEntityById(Long productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}

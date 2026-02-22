package com.example.ecommerce_clean.modules.product.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_clean.modules.product.application.dto.CategoryRequest;
import com.example.ecommerce_clean.modules.product.application.dto.CategoryResponse;
import com.example.ecommerce_clean.modules.product.application.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@RequestBody CategoryRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return service.getAll();
    }
}

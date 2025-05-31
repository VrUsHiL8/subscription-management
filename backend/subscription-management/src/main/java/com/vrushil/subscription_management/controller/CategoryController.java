package com.vrushil.subscription_management.controller;

import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.CategoryResponse;
import com.vrushil.subscription_management.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200/dashboard")
@RestController
@RequestMapping("/api/user/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Get category by ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long categoryId) {
        ApiResponse<CategoryResponse> response = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(response);
    }

    // Get all categories
    @GetMapping("/all-categories")
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getAllCategories(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        ApiResponse<Page<CategoryResponse>> response = categoryService.getAllCategories(search,pageable);
        return ResponseEntity.ok(response);
    }
}

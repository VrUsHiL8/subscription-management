package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.request.CreateCategoryRequest;
import com.vrushil.subscription_management.request.UpdateCategoryRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    ApiResponse<CategoryResponse> createCategory(CreateCategoryRequest createCategoryRequest);
    ApiResponse<CategoryResponse> getCategoryById(Long id);
    ApiResponse<Page<CategoryResponse>> getAllCategories(String search,Pageable pageable);
    ApiResponse<CategoryResponse> updateCategory(Long id, UpdateCategoryRequest updatedCategory);
    ApiResponse<String> deleteCategory(Long id);
}
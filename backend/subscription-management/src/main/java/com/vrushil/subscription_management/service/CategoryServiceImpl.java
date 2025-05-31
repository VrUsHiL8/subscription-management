package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.entity.Category;
import com.vrushil.subscription_management.exception.AlreadyExistsException;
import com.vrushil.subscription_management.exception.CategoryNotFoundException;
import com.vrushil.subscription_management.repository.CategoryRepository;
import com.vrushil.subscription_management.request.CreateCategoryRequest;
import com.vrushil.subscription_management.request.UpdateCategoryRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.CategoryResponse;
import com.vrushil.subscription_management.util.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ApiResponse<CategoryResponse> createCategory(CreateCategoryRequest createCategoryRequest) {
        if (createCategoryRequest.getCategoryType() == null || createCategoryRequest.getCategoryType().isBlank()) {
            throw new IllegalArgumentException("Category type cannot be empty or null.");
        }
        if (createCategoryRequest.getDescription() == null || createCategoryRequest.getDescription().isBlank()) {
            throw new IllegalArgumentException("Category description cannot be empty or null.");
        }

        Category existingCategory = categoryRepository.findByCategoryType(createCategoryRequest.getCategoryType());
        if (existingCategory != null) {
            throw new AlreadyExistsException("Category already exists.");
        }

        Category category = new Category();
        category.setCategoryType(createCategoryRequest.getCategoryType());
        category.setDescription(createCategoryRequest.getDescription());

        Category savedCategory = categoryRepository.save(category);

        CategoryResponse response = Mapper.toCategoryResponse(savedCategory);
        return new ApiResponse<>("SUCCESS", "Category created successfully", response);
    }

    @Override
    public ApiResponse<CategoryResponse> getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        CategoryResponse response = Mapper.toCategoryResponse(category);
        return new ApiResponse<>("SUCCESS", "Category fetched successfully", response);
    }

    @Override
    public ApiResponse<Page<CategoryResponse>> getAllCategories(String search,Pageable pageable) {
        Page<Category> categories;

        if (search != null && !search.isBlank()) {
            categories = categoryRepository.findByCategoryTypeContainingIgnoreCase(search, pageable);
        } else {
            categories = categoryRepository.findAll(pageable);
        }

        Page<CategoryResponse> categoryResponses = categories
                .map(Mapper::toCategoryResponse);

        return new ApiResponse<>(
                "SUCCESS",
                "All categories fetched successfully",
                categoryResponses);
    }

    @Override
    public ApiResponse<CategoryResponse> updateCategory(Long categoryId, UpdateCategoryRequest updatedCategory) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        if (updatedCategory.getCategoryType() != null) {
            existingCategory.setCategoryType(updatedCategory.getCategoryType());
        }
        if (updatedCategory.getDescription() != null) {
            existingCategory.setDescription(updatedCategory.getDescription());
        }

        Category updated = categoryRepository.save(existingCategory);
        CategoryResponse response = Mapper.toCategoryResponse(updated);
        return new ApiResponse<>("SUCCESS", "Category updated successfully", response);
    }

    @Override
    public ApiResponse<String> deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));
        categoryRepository.deleteById(categoryId);
        return new ApiResponse<>("SUCCESS", "Category deleted successfully", "Category with ID " + categoryId + " deleted.");
    }
}

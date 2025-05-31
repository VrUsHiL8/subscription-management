package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.entity.*;
import com.vrushil.subscription_management.exception.AlreadyExistsException;
import com.vrushil.subscription_management.exception.AppNotFoundException;
import com.vrushil.subscription_management.exception.CategoryNotFoundException;
import com.vrushil.subscription_management.repository.*;
import com.vrushil.subscription_management.request.AppRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.AppResponse;
import com.vrushil.subscription_management.util.Mapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AppServiceImpl implements AppService {

    private final AppRepository appRepository;
    private final CategoryRepository categoryRepository;

    public AppServiceImpl(AppRepository appRepository,
                          CategoryRepository categoryRepository) {
        this.appRepository = appRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public ApiResponse<AppResponse> createApp(AppRequest appRequest) {
        if (appRepository.existsByAppName(appRequest.getAppName())) {
            return new ApiResponse<>("ERROR", "App with name " + appRequest.getAppName() + " already exists.", null);
        }

        Category category = categoryRepository.findById(appRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        App app = new App();
        app.setAppName(appRequest.getAppName());
        app.setLogoUrl(appRequest.getLogoUrl());
        app.setWebsite(appRequest.getWebsite());
        app.setCategory(category);

        App savedApp = appRepository.save(app);
        return new ApiResponse<>("SUCCESS", "App created successfully", Mapper.toAppResponse(savedApp));
    }

    @Override
    public ApiResponse<AppResponse> getAppById(Long appId) {
        App app = appRepository.findById(appId)
                .orElseThrow(() -> new AppNotFoundException("App not found"));
        return new ApiResponse<>("SUCCESS", "App fetched successfully", Mapper.toAppResponse(app));
    }

    @Override
    public ApiResponse<Page<AppResponse>> getAllApps(String search, Pageable pageable) {
        Page<App> apps;

        if(search != null && !search.isBlank()) {
            // 1. Try searching by App Name for this user
            apps = appRepository.findByAppNameContainingIgnoreCase(search, pageable);

            // 2. If no results, try Category
            if (apps.isEmpty()) {
                apps = appRepository.findByCategory_CategoryTypeContainingIgnoreCase(search, pageable);
            }
        } else {
            apps = appRepository.findAll(pageable);
        }
        Page<AppResponse> appResponse = apps
                .map(Mapper::toAppResponse);
        return new ApiResponse<>("SUCCESS", "All apps fetched successfully", appResponse);
    }


    @Override
    public ApiResponse<Page<AppResponse>> getAppByCategoryId(Pageable pageable,Long categoryId) {
        Page<App> appPage = appRepository.findByCategory_CategoryId(pageable,categoryId);
        Page<AppResponse> appResponses = appPage
                .map(Mapper::toAppResponse);
        return new ApiResponse<>("SUCCESS", "Apps fetched successfully", appResponses);
    }

    @Override
    @Transactional
    public ApiResponse<AppResponse> updateApp(Long appId, AppRequest updateAppRequest) {
        App existingApp = appRepository.findById(appId)
                .orElseThrow(() -> new AppNotFoundException("App not found"));
        if (!existingApp.getAppName().equals(updateAppRequest.getAppName()) &&
                appRepository.existsByAppName(updateAppRequest.getAppName())) {
            throw new AlreadyExistsException("App with name " + updateAppRequest.getAppName() + " already exists.");
        }

        Category category = categoryRepository.findById(updateAppRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingApp.setAppName(updateAppRequest.getAppName());
        existingApp.setCategory(category);
        existingApp.setLogoUrl(updateAppRequest.getLogoUrl());
        existingApp.setWebsite(updateAppRequest.getWebsite());
        App updatedApp = appRepository.save(existingApp);

        return new ApiResponse<>("SUCCESS", "App updated successfully", Mapper.toAppResponse(updatedApp));
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteApp(Long appId) {
    App app = appRepository.findById(appId)
            .orElseThrow(() -> new AppNotFoundException("App not found"));
        appRepository.deleteById(appId);
        return new ApiResponse<>("SUCCESS", "App deleted successfully", null);
    }

}

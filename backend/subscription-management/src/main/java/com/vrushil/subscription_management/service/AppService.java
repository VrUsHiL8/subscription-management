package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.request.AppRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.AppResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AppService {
    ApiResponse<AppResponse> createApp(AppRequest appRequest);
    ApiResponse<AppResponse> getAppById(Long id);
    ApiResponse<Page<AppResponse>> getAllApps(String search,Pageable pageable);
    ApiResponse<AppResponse> updateApp(Long id, AppRequest updateAppRequest);
    ApiResponse<String> deleteApp(Long id);

    ApiResponse<Page<AppResponse>> getAppByCategoryId(Pageable pageable,Long categoryId);
}

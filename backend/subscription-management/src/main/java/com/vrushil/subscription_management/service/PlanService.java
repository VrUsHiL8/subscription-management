package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.request.PlanRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.PlanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlanService {
    ApiResponse<PlanResponse> createPlan(PlanRequest request);
    ApiResponse<PlanResponse> getPlanById(Long id);
    ApiResponse<Page<PlanResponse>>  getAllPlans(String search,Pageable pageable);
    ApiResponse<PlanResponse> updatePlan(Long id, PlanRequest updatedPlan);
    ApiResponse<String> deletePlan(Long id);

    ApiResponse<Page<PlanResponse>> getPlanByAppId(Pageable pageable,Long appId);
}

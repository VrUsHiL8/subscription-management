package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.entity.*;
import com.vrushil.subscription_management.exception.AlreadyExistsException;
import com.vrushil.subscription_management.exception.AppNotFoundException;
import com.vrushil.subscription_management.exception.PlanNotFoundException;
import com.vrushil.subscription_management.repository.AppRepository;
import com.vrushil.subscription_management.repository.PlanRepository;
import com.vrushil.subscription_management.request.PlanRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.PlanResponse;
import com.vrushil.subscription_management.util.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final AppRepository appRepository;

    public PlanServiceImpl(PlanRepository planRepository,
                           AppRepository appRepository) {
        this.planRepository = planRepository;
        this.appRepository = appRepository;
    }

    @Override
    public ApiResponse<PlanResponse> createPlan(PlanRequest planRequest) {
        App app = appRepository.findById(planRequest.getAppId())
                .orElseThrow(() -> new AppNotFoundException("App not found with ID: " + planRequest.getAppId()));

        PlanType planType = planRequest.getPlanType();
        if (planType == null) {
            throw new IllegalArgumentException("Plan type is required.");
        }
        Plan existingPlan = planRepository.findByPlanType(planRequest.getPlanType());
        if(existingPlan!=null){
            throw new AlreadyExistsException("Plan Type already exists");
        }
        Plan plan = new Plan();
        plan.setBillingCycle(planRequest.getBillingCycle());
        plan.setPrice(planRequest.getPrice());
        plan.setFeatures(planRequest.getFeatures());
        plan.setPlanType(planType);
        plan.setApp(app);

        Plan savedPlan = planRepository.save(plan);
        PlanResponse response = Mapper.toPlanResponse(savedPlan);

        return new ApiResponse<>("SUCCESS", "Plan created successfully", response);
    }

    @Override
    public ApiResponse<PlanResponse> getPlanById(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException("Subscription Plan not found with ID: " + planId));
        return new ApiResponse<>("SUCCESS", "Plan retrieved successfully", Mapper.toPlanResponse(plan));
    }

    @Override
    public ApiResponse<Page<PlanResponse>> getAllPlans(String search,Pageable pageable) {
        Page<Plan> plans;
        if (search!= null && !search.isEmpty()) {
            // Try searching by app name
            plans = planRepository.findAllByApp_AppNameContainingIgnoreCase(search, pageable);

            // If no results, try category
            if (plans.isEmpty()) {
                plans = planRepository.findAllByApp_Category_CategoryTypeContainingIgnoreCase(search, pageable);
            }

            // If still no results, try plan
            if (plans.isEmpty()) {
                plans = planRepository.findAllByPlanTypeLike(search,pageable);
            }
        } else {
            plans = planRepository.findAll(pageable);
        }
        Page<PlanResponse> planResponses = plans
                .map(Mapper::toPlanResponse);
        return new ApiResponse<>("SUCCESS", "Plans retrieved successfully",  planResponses);
    }

    @Override
    public ApiResponse<Page<PlanResponse>> getPlanByAppId(Pageable pageable,Long appId) {
        Page<Plan> plan = planRepository.findByApp_AppId(pageable,appId);
        Page<PlanResponse> planResponses = plan
                .map(Mapper::toPlanResponse);
        return new ApiResponse<>("SUCCESS", "Plan retrieved successfully", planResponses);
    }

    @Override
    public ApiResponse<PlanResponse> updatePlan(Long planId, PlanRequest updatedPlan) {
        Plan existingPlan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException("Subscription Plan not found with ID: " + planId));

        if (updatedPlan.getAppId() != null) {
            App app = appRepository.findById(updatedPlan.getAppId())
                    .orElseThrow(() -> new AppNotFoundException("App not found"));
            existingPlan.setApp(app);
        }

        if (updatedPlan.getBillingCycle() != null) {
            existingPlan.setBillingCycle(updatedPlan.getBillingCycle());
        }

        if (updatedPlan.getPrice() != null && updatedPlan.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            existingPlan.setPrice(updatedPlan.getPrice());
        }

        if (updatedPlan.getFeatures() != null) {
            existingPlan.setFeatures(updatedPlan.getFeatures());
        }

        Plan updatedEntity = planRepository.save(existingPlan);
        return new ApiResponse<>("SUCCESS", "Plan updated successfully", Mapper.toPlanResponse(updatedEntity));
    }

    @Override
    public ApiResponse<String> deletePlan(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException("Subscription Plan not found with ID: " + planId));
        planRepository.delete(plan);
        return new ApiResponse<>("SUCCESS", "Plan deleted successfully", null);
    }

}



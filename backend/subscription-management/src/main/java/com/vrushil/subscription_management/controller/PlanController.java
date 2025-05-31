package com.vrushil.subscription_management.controller;

import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.PlanResponse;
import com.vrushil.subscription_management.service.PlanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService){
        this.planService = planService;
    }

    //Get Plan by id
    @GetMapping("/{planId}")
    public ResponseEntity<ApiResponse<PlanResponse>> getPlanById(@PathVariable Long planId) {
        ApiResponse<PlanResponse> response = planService.getPlanById(planId);
        return ResponseEntity.ok(response);
    }

    //Get Plan by id
    @GetMapping("/apps/{appId}")
    public ResponseEntity<ApiResponse<Page<PlanResponse>>> getPlanByAppId(@PathVariable Long appId,Pageable pageable) {
        ApiResponse<Page<PlanResponse>> response = planService.getPlanByAppId(pageable,appId);
        return ResponseEntity.ok(response);
    }

    //Get All Plans
    @GetMapping("/all-plans")
    public ResponseEntity<ApiResponse<Page<PlanResponse>>> getAllPlans(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        ApiResponse<Page<PlanResponse>> response = planService.getAllPlans(search,pageable);
        return ResponseEntity.ok(response);
    }

}

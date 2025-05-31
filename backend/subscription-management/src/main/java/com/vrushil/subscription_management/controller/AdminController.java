package com.vrushil.subscription_management.controller;

import com.vrushil.subscription_management.request.*;
import com.vrushil.subscription_management.response.*;
import com.vrushil.subscription_management.service.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final AuthService authService;
    private final SubscriptionService subscriptionService;
    private final CategoryService categoryService;
    private final RenewalService renewalService;
    private final AppService appService;
    private final PlanService planService;

    public AdminController(UserService userService
            , AuthService authService
            , SubscriptionService subscriptionService
            , CategoryService categoryService
            , RenewalService renewalService
            , AppService appService
            , PlanService planService){
        this.userService=userService;
        this.authService=authService;
        this.subscriptionService=subscriptionService;
        this.categoryService = categoryService;
        this.renewalService = renewalService;
        this.appService=appService;
        this.planService = planService;
    }

    //Get all Users
    @GetMapping("/all-users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(required = false) String search,
            Pageable pageable){
        ApiResponse<Page<UserResponse>> response = userService.getAllUsers(search,pageable);
        return ResponseEntity.ok(response);
    }

    //Get all Subscriptions
    @GetMapping("/all-subscriptions")
    public ResponseEntity<ApiResponse<Page<SubscriptionResponse>>> getAllSubscriptions(
            @RequestParam(required = false) String search,
            Pageable pageable){
        ApiResponse<Page<SubscriptionResponse>> response = subscriptionService.getAllSubscriptions(search,pageable);
        return ResponseEntity.ok(response);
    }

    //Create a new category
    @PostMapping("/categories/create")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody CreateCategoryRequest request) {
        ApiResponse<CategoryResponse> response = categoryService.createCategory(request);
        return ResponseEntity.ok(response);
    }

    //Update category
    @PutMapping("/categories/update/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long categoryId, @RequestBody UpdateCategoryRequest updatedCategory) {
        ApiResponse<CategoryResponse> response = categoryService.updateCategory(categoryId, updatedCategory);
        return ResponseEntity.ok(response);
    }

    //Delete category
    @DeleteMapping("/categories/delete/{categoryId}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long categoryId) {
        ApiResponse<String> response = categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("renewals/all-renewals/{id}")
    public ResponseEntity<ApiResponse<Page<RenewalResponse>>> getAllRenewalsForUser(
            @RequestParam (required = false) String search,
            @PathVariable Long id,
            Pageable pageable) {
        ApiResponse<Page<RenewalResponse>> response = renewalService.getAllRenewalsForId(search,id, pageable);
        return ResponseEntity.ok(response);
    }


    // Role Update
    @PostMapping("/update-role/{id}")
    public ResponseEntity<ApiResponse<UpdateRoleResponse>> updateUserRole(@PathVariable Long id, @RequestBody UpdateRoleRequest role) {
        ApiResponse<UpdateRoleResponse> user = authService.updateUserRole(id, role);
        return ResponseEntity.ok(user);
    }

    //Renewal Approval
    @PutMapping("/renewals/{renewalId}/approve")
    public ResponseEntity<ApiResponse<String>> approveRenewal(@PathVariable Long renewalId) {
        ApiResponse<String> response = renewalService.approveRenewal(renewalId);
        return ResponseEntity.ok(response);
    }

    //Create App
    @PostMapping("/apps/create")
    public ResponseEntity<ApiResponse<AppResponse>> createApp(@RequestBody AppRequest appRequest) {
        ApiResponse<AppResponse> response = appService.createApp(appRequest);
        return ResponseEntity.ok(response);
    }

    //Update App
    @PutMapping("/apps/{appId}")
    public ResponseEntity<ApiResponse<AppResponse>> updateApp(@PathVariable Long appId, @RequestBody AppRequest updateAppRequest) {
        ApiResponse<AppResponse> response = appService.updateApp(appId, updateAppRequest);
        return ResponseEntity.ok(response);
    }

    // Delete App
    @DeleteMapping("/apps/{appId}")
    public ResponseEntity<ApiResponse<String>> deleteApp(@PathVariable Long appId) {
        ApiResponse<String> response = appService.deleteApp(appId);
        return ResponseEntity.ok(response);
    }

    //Create new Plan
    @PostMapping("/plans/create")
    public ResponseEntity<ApiResponse<PlanResponse>> createPlan(@RequestBody PlanRequest planRequest) {
        ApiResponse<PlanResponse> response = planService.createPlan(planRequest);
        return ResponseEntity.ok(response);
    }

    //Update Plan
    @PutMapping("/plans/{planId}")
    public ResponseEntity<ApiResponse<PlanResponse>> updatePlan(@PathVariable Long planId, @RequestBody PlanRequest updatedPlan) {
        ApiResponse<PlanResponse> response = planService.updatePlan(planId, updatedPlan);
        return ResponseEntity.ok(response);
    }

    //Delete Plan
    @DeleteMapping("/plans/{planId}")
    public ResponseEntity<ApiResponse<String>> deletePlan(@PathVariable Long planId) {
        ApiResponse<String> response = planService.deletePlan(planId);
        return ResponseEntity.ok(response);
    }

}

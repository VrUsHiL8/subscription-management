package com.vrushil.subscription_management.controller;

import com.vrushil.subscription_management.entity.SubscriptionStatus;
import com.vrushil.subscription_management.request.SubscriptionRequest;
import com.vrushil.subscription_management.request.SubscriptionUpdateRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.SubscriptionResponse;
import com.vrushil.subscription_management.response.SubscriptionUpdateResponse;
import com.vrushil.subscription_management.service.SubscriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/user/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // Create a new subscription
    @PostMapping("/subscribe")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> createSubscription(@RequestBody SubscriptionRequest request) {
        ApiResponse<SubscriptionResponse> response = subscriptionService.createSubscription(request);
        return ResponseEntity.ok(response);
    }

    // Get Subscription by ID
    @GetMapping("/{subscriptionId}")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> getSubscriptionById(@PathVariable Long subscriptionId) {
        ApiResponse<SubscriptionResponse> response = subscriptionService.getSubscriptionById(subscriptionId);
        return ResponseEntity.ok(response);
    }

    // Get all Subscriptions for a User ID
    @GetMapping("/all-subscriptions/{subscriptionId}")
    public ResponseEntity<ApiResponse<Page<SubscriptionResponse>>> getAllSubscriptionsForId(
            @RequestParam (required = false) String search,
            @RequestParam (required = false) SubscriptionStatus status,
            @PathVariable Long subscriptionId, Pageable pageable) {
        ApiResponse<Page<SubscriptionResponse>> response = subscriptionService.getAllSubscriptionsForId(search ,subscriptionId ,status ,pageable);
        return ResponseEntity.ok(response);
    }

    // Update Subscription
    @PutMapping("/update/{subscriptionId}")
    public ResponseEntity<ApiResponse<SubscriptionUpdateResponse>> updateSubscription(@PathVariable Long subscriptionId, @RequestBody SubscriptionUpdateRequest updateRequest) {
        ApiResponse<SubscriptionUpdateResponse> response = subscriptionService.updateSubscription(subscriptionId, updateRequest);
        return ResponseEntity.ok(response);
    }

    // Delete Subscription
    @DeleteMapping("/delete/{subscriptionId}")
    public ResponseEntity<ApiResponse<String>> deleteSubscription(@PathVariable Long subscriptionId) {
        ApiResponse<String> response = subscriptionService.deleteSubscription(subscriptionId);
        return ResponseEntity.ok(response);
    }
}

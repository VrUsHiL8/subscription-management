package com.vrushil.subscription_management.controller;

import com.vrushil.subscription_management.request.RenewalRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.RenewalResponse;
import com.vrushil.subscription_management.response.SubscriptionResponse;
import com.vrushil.subscription_management.service.RenewalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200/dashboard")
@RestController
@RequestMapping("/api/user/renewals")
public class RenewalController {

    private final RenewalService renewalService;

    public RenewalController(RenewalService renewalService){
        this.renewalService = renewalService;
    }

    //Create a new renewal
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<RenewalResponse>> createRenewal(@RequestBody RenewalRequest renewalRequest){
        ApiResponse<RenewalResponse> response = renewalService.createRenewal(renewalRequest);
        return ResponseEntity.ok(response);
    }

    //Get a renewal by id
    @GetMapping("/{renewalId}")
    public ResponseEntity<ApiResponse<RenewalResponse>> getRenewalById(@PathVariable Long renewalId){
        ApiResponse<RenewalResponse> response = renewalService.getRenewalById(renewalId);
        return ResponseEntity.ok(response);
    }

    //Get all renewals
    @GetMapping("/all-renewals")
    public ResponseEntity<ApiResponse<Page<RenewalResponse>>> getAllRenewals(
            @RequestParam(required = false) String search,
            Pageable pageable){
        ApiResponse<Page<RenewalResponse>> response = renewalService.getAllRenewals(search,pageable);
        return ResponseEntity.ok(response);
    }

    //Get all Renewals for a User
    @GetMapping("/all-renewals/{userId}")
    public ResponseEntity<ApiResponse<Page<RenewalResponse>>> getAllRenewalsForId(
            @RequestParam (required = false) String search,
            @PathVariable Long userId, Pageable pageable){
        ApiResponse<Page<RenewalResponse>> response = renewalService.getAllRenewalsForId(search,userId,pageable);
        return ResponseEntity.ok(response);
    }

    // Upcoming Renewals
    @GetMapping("/upcoming/{userId}")
    public ResponseEntity<ApiResponse<Page<SubscriptionResponse>>> getUpcomingRenewals(
            @RequestParam(required = false) String search,
            @PathVariable Long userId,
            Pageable pageable) {
        ApiResponse<Page<SubscriptionResponse>> response = renewalService.getUpcomingRenewalsForUser(search, userId, pageable);
        return ResponseEntity.ok(response);
    }

    // Expired Renewals
    @GetMapping("/expired/{userId}")
    public ResponseEntity<ApiResponse<Page<SubscriptionResponse>>> getExpiredRenewals(
            @RequestParam(required = false) String search,
            @PathVariable Long userId,
            Pageable pageable) {
        ApiResponse<Page<SubscriptionResponse>> response = renewalService.getExpiredRenewalsForUser(search, userId, pageable);
        return ResponseEntity.ok(response);
    }

    // Early Renewals
    @GetMapping("/early/{userId}")
    public ResponseEntity<ApiResponse<Page<SubscriptionResponse>>> getEarlyRenewals(
            @RequestParam(required = false) String search,
            @PathVariable Long userId,
            Pageable pageable) {
        ApiResponse<Page<SubscriptionResponse>> response = renewalService.getEarlyRenewalsForUser(search, userId, pageable);
        return ResponseEntity.ok(response);
    }

    //Delete a renewal
    @DeleteMapping("/delete/{renewalId}")
    public  ResponseEntity<ApiResponse<String>> deleteRenewal(@PathVariable Long renewalId){
        ApiResponse<String> response = renewalService.deleteRenewal(renewalId);
        return ResponseEntity.ok(response);
    }
}

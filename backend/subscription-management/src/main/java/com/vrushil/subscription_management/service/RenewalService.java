package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.request.RenewalRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.RenewalResponse;
import com.vrushil.subscription_management.response.SubscriptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RenewalService {
    ApiResponse<RenewalResponse> createRenewal(RenewalRequest request);
    ApiResponse<RenewalResponse> getRenewalById(Long id);
    ApiResponse<Page<RenewalResponse>> getAllRenewals(String search,Pageable pageable);
    ApiResponse<Page<RenewalResponse>> getAllRenewalsForId(String search,Long id, Pageable pageable);
    ApiResponse<String> deleteRenewal(Long id);
    ApiResponse<String> approveRenewal(Long renewalId);
    ApiResponse<Page<SubscriptionResponse>> getUpcomingRenewalsForUser(String search, Long userId, Pageable pageable);
    ApiResponse<Page<SubscriptionResponse>> getExpiredRenewalsForUser(String search, Long userId, Pageable pageable);
    ApiResponse<Page<SubscriptionResponse>> getEarlyRenewalsForUser(String search, Long userId, Pageable pageable);
}

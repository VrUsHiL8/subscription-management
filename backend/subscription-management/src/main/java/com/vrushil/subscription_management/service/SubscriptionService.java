package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.entity.SubscriptionStatus;
import com.vrushil.subscription_management.request.SubscriptionRequest;
import com.vrushil.subscription_management.request.SubscriptionUpdateRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.SubscriptionResponse;
import com.vrushil.subscription_management.response.SubscriptionUpdateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriptionService {
    ApiResponse<SubscriptionResponse> createSubscription(SubscriptionRequest request);
    ApiResponse<SubscriptionResponse> getSubscriptionById(Long id);
    ApiResponse<Page<SubscriptionResponse>> getAllSubscriptions(String search,Pageable pageable);
    ApiResponse<Page<SubscriptionResponse>> getAllSubscriptionsForId(String search, Long userid, SubscriptionStatus status, Pageable pageable);
    ApiResponse<SubscriptionUpdateResponse> updateSubscription(Long id, SubscriptionUpdateRequest updateRequest);
    ApiResponse<String> deleteSubscription(Long id);
}


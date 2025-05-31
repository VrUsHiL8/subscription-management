package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.exception.*;
import com.vrushil.subscription_management.response.SubscriptionResponse;
import com.vrushil.subscription_management.entity.*;
import com.vrushil.subscription_management.repository.*;
import com.vrushil.subscription_management.request.SubscriptionRequest;
import com.vrushil.subscription_management.request.SubscriptionUpdateRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.SubscriptionUpdateResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import com.vrushil.subscription_management.util.Mapper;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PlanRepository planRepository;
    private final AppRepository appRepository;

    public SubscriptionServiceImpl(
            SubscriptionRepository subscriptionRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            PlanRepository planRepository,
            AppRepository appRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.planRepository = planRepository;
        this.appRepository = appRepository;
    }

    @Override
    @Transactional
    public ApiResponse<SubscriptionResponse> createSubscription(SubscriptionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new PlanNotFoundException("Subscription Plan not found"));
        App app = appRepository.findById(request.getAppId())
                .orElseThrow(() -> new AppNotFoundException("App not Found"));

        // ✅ Check if the user is already subscribed to this plan
        boolean isSubscribed = subscriptionRepository.existsByUser_UserIdAndPlan_PlanId(
                request.getUserId(), request.getPlanId());

        if (isSubscribed) {
            return new ApiResponse<>("EXISTS", "User is already subscribed to this plan", null);
        }

        // ✅ Create a new subscription
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = calculateEndDate(startDate, plan.getBillingCycle());

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setCategory(category);
        subscription.setPlan(plan);
        subscription.setApp(app);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        SubscriptionResponse response = Mapper.toSubscriptionResponse(savedSubscription);
        return new ApiResponse<>("SUCCESS", "Subscription created successfully", response);
    }

    @Override
    public ApiResponse<SubscriptionResponse> getSubscriptionById(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));
        SubscriptionResponse response = Mapper.toSubscriptionResponse(subscription);
        return new ApiResponse<>("SUCCESS", "Subscription fetched successfully", response);
    }

    @Override
    public ApiResponse<Page<SubscriptionResponse>> getAllSubscriptions(String search, Pageable pageable) {
        Page<Subscription> subscriptions;

        if (search != null && !search.isBlank()) {
            // Try searching by app name
            subscriptions = subscriptionRepository.findAllByApp_AppNameContainingIgnoreCase(search, pageable);

            // If no results, try category
            if (subscriptions.isEmpty()) {
                subscriptions = subscriptionRepository.findAllByCategory_CategoryTypeContainingIgnoreCase(search, pageable);
            }

            // If still no results, try plan
            if (subscriptions.isEmpty()) {
                subscriptions = subscriptionRepository.findAllByPlanTypeLike(search, pageable);
            }
        } else {
            subscriptions = subscriptionRepository.findAll(pageable);
        }

        Page<SubscriptionResponse> responses = subscriptions
                .map(Mapper::toSubscriptionResponse);

        return new ApiResponse<>("SUCCESS", "All subscriptions fetched successfully", responses);
    }


    public ApiResponse<Page<SubscriptionResponse>> getAllSubscriptionsForId(String search, Long userId, SubscriptionStatus status, Pageable pageable) {
        Page<Subscription> subscriptions;

        boolean filterByStatus = status != null;

        if (search != null && !search.isBlank()) {
            if (filterByStatus) {
                subscriptions = subscriptionRepository.findByUser_UserIdAndStatusAndApp_AppNameContainingIgnoreCase(userId, status, search, pageable);

                if (subscriptions.isEmpty()) {
                    subscriptions = subscriptionRepository.findByUser_UserIdAndStatusAndCategory_CategoryTypeContainingIgnoreCase(userId, status, search, pageable);
                }

                if (subscriptions.isEmpty()) {
                    subscriptions = subscriptionRepository.findByUser_UserIdAndStatusAndPlan_PlanTypeContainingIgnoreCase(userId, status, search, pageable);
                }
            } else {
                subscriptions = subscriptionRepository.findByUser_UserIdAndApp_AppNameContainingIgnoreCase(userId, search, pageable);

                if (subscriptions.isEmpty()) {
                    subscriptions = subscriptionRepository.findByUser_UserIdAndCategory_CategoryTypeContainingIgnoreCase(userId, search, pageable);
                }

                if (subscriptions.isEmpty()) {
                    subscriptions = subscriptionRepository.findByUser_UserIdAndPlan_PlanTypeContainingIgnoreCase(userId, search, pageable);
                }
            }
        } else {
            subscriptions = filterByStatus
                    ? subscriptionRepository.findByUser_UserIdAndStatus(userId, status, pageable)
                    : subscriptionRepository.findByUser_UserId(userId, pageable);
        }

        Page<SubscriptionResponse> responses = subscriptions.map(Mapper::toSubscriptionResponse);

        return new ApiResponse<>("SUCCESS", "User subscriptions fetched successfully", responses);
    }

    @Override
    @Transactional
    public ApiResponse<SubscriptionUpdateResponse> updateSubscription(Long subscriptionId, SubscriptionUpdateRequest updateRequest) {
        Subscription existingSubscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));

        if (updateRequest.getStartDate() != null) {
            existingSubscription.setStartDate(updateRequest.getStartDate());
        }
        if (updateRequest.getEndDate() != null) {
            existingSubscription.setEndDate(updateRequest.getEndDate());
        }
        if (updateRequest.getStatus() != null) {
            existingSubscription.setStatus(updateRequest.getStatus());
        }
        if (updateRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateRequest.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
            existingSubscription.setCategory(category);
        }
        if (updateRequest.getPlanId() != null) {
            Plan plan = planRepository.findById(updateRequest.getPlanId())
                    .orElseThrow(() -> new PlanNotFoundException("Subscription Plan not found"));
            existingSubscription.setPlan(plan);
        }

        Subscription updatedSubscription = subscriptionRepository.save(existingSubscription);
        SubscriptionUpdateResponse response = Mapper.toSubscriptionUpdateResponse(updatedSubscription);
        return new ApiResponse<>("SUCCESS", "Subscription updated successfully", response);
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteSubscription(Long subscriptionId) {
    Subscription subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));
        subscriptionRepository.deleteById(subscriptionId);
        return new ApiResponse<>("SUCCESS", "Subscription deleted successfully", "Subscription with ID " + subscriptionId + " deleted.");
    }

    private LocalDate calculateEndDate(LocalDate startDate, BillingCycle cycle) {
        return switch (cycle) {
            case MONTHLY -> startDate.plusMonths(1);
            case QUARTERLY -> startDate.plusMonths(3);
            case YEARLY -> startDate.plusYears(1);
        };
    }
}


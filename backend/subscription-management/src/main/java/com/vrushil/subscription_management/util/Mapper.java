package com.vrushil.subscription_management.util;

import com.vrushil.subscription_management.entity.*;
import com.vrushil.subscription_management.request.SSOUserRequest;
import com.vrushil.subscription_management.request.UserRegisterRequest;
import com.vrushil.subscription_management.response.*;

public class Mapper {

    public static AppResponse toAppResponse(App app) {
        Category category = app.getCategory();
        return AppResponse.builder()
                .appId(app.getAppId())
                .appName(app.getAppName())
                .categoryId(category.getCategoryId())
                .categoryType(category.getCategoryType())
                .logoUrl(app.getLogoUrl())
                .website(app.getWebsite())
                .build();
    }

    public static PlanResponse toPlanResponse(Plan plan) {
        return PlanResponse.builder()
                .planId(plan.getPlanId())
                .appId(plan.getApp().getAppId())
                .categoryId(plan.getApp().getCategory().getCategoryId())
                .categoryType(plan.getApp().getCategory().getCategoryType())
                .appName(plan.getApp().getAppName())
                .price(plan.getPrice())
                .billingCycle(plan.getBillingCycle())
                .features(plan.getFeatures())
                .planType(plan.getPlanType())
                .build();
    }

    public static UserResponse toUserResponse(User user, String loginMethod, String ssoProvider) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .loginMethod(loginMethod)
                .ssoProvider(ssoProvider)
                .build();
    }

    public static SubscriptionResponse toSubscriptionResponse(Subscription subscription) {
        return SubscriptionResponse.builder()
                .subscriptionId(subscription.getSubscriptionId())
                .userId(subscription.getUser().getUserId())
                .planId(subscription.getPlan().getPlanId())
                .appId(subscription.getApp().getAppId())
                .categoryId(subscription.getCategory().getCategoryId())
                .appName(subscription.getAppName())
                .categoryType(subscription.getCategoryType())
                .price(subscription.getPrice())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .status(subscription.getStatus())
                .planType(subscription.getPlanType())
                .build();
    }

    public static SubscriptionUpdateResponse toSubscriptionUpdateResponse(Subscription subscription) {
        return SubscriptionUpdateResponse.builder()
                .subscriptionId(subscription.getSubscriptionId())
                .appName(subscription.getAppName())
                .categoryType(subscription.getCategoryType())
                .price(subscription.getPrice())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .status(subscription.getStatus())
                .planType(subscription.getPlanType())
                .build();
    }

    public static CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryType(category.getCategoryType())
                .description(category.getDescription())
                .build();
    }

    public static RenewalResponse toRenewalResponse(Renewal renewal) {
        Plan plan = renewal.getSubscription().getPlan();
        App app = plan.getApp();

        return RenewalResponse.builder()
                .renewalId(renewal.getRenewalId())
                .userId(renewal.getSubscription().getUser().getUserId())
                .subscriptionId(renewal.getSubscription().getSubscriptionId())
                .appId(app !=null ? app.getAppId() : null)
                .appName(app !=null ? app.getAppName() : null)
                .website(app !=null ? app.getWebsite() : null)
                .planId(plan.getPlanId())
                .planType(plan.getPlanType())
                .price(plan.getPrice())
                .billingCycle(plan.getBillingCycle())
                .features(plan.getFeatures())
                .renewalDate(renewal.getRenewalDate())
                .approved(renewal.getStatus() == TransactionStatus.SUCCESS)
                .transactionStatus(renewal.getStatus())
                .build();
    }

    public static AuthResponse toAuthResponse(String accessToken, String refreshToken, String loginMethod,Role role, String message) {
        return new AuthResponse(accessToken, refreshToken, loginMethod,role, message);
    }

    public static UserRegisterResponse toRegisterResponse(User user) {
        return UserRegisterResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole()!= null ? user.getRole().name() : Role.USER.name())
                .build();
    }

    public static UpdateRoleResponse toUpdateRoleResponse(User user) {
        return UpdateRoleResponse.builder()
                .userId(user.getUserId())
                .role(user.getRole())
                .build();
    }

    public static User toUserEntity(UserRegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .build();
    }

    public static User toUserEntity(SSOUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(Role.USER)
                .build();
    }

}

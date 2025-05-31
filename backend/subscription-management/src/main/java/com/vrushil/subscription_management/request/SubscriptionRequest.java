package com.vrushil.subscription_management.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequest {

    @NotNull
    private Long userId;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long planId;
    @NotNull
    private Long appId;

}

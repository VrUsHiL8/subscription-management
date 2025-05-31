package com.vrushil.subscription_management.request;

import com.vrushil.subscription_management.entity.App;
import com.vrushil.subscription_management.entity.SubscriptionStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SubscriptionUpdateRequest {

    private App app;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscriptionStatus status;
    private Long categoryId;
    private Long planId;

}

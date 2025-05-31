package com.vrushil.subscription_management.response;

import com.vrushil.subscription_management.entity.PlanType;
import com.vrushil.subscription_management.entity.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionUpdateResponse {
    private Long subscriptionId;
    private String appName;
    private String categoryType;
    private BigDecimal price;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscriptionStatus status;
    private PlanType planType;
}
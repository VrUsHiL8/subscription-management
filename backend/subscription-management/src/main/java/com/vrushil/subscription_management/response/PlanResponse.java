package com.vrushil.subscription_management.response;

import com.vrushil.subscription_management.entity.BillingCycle;
import com.vrushil.subscription_management.entity.PlanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanResponse {

    private Long planId;
    private Long appId;
    private Long categoryId;
    private String appName;
    private BigDecimal price;
    private BillingCycle billingCycle;
    private String features;
    private PlanType planType;
    private String categoryType;
}

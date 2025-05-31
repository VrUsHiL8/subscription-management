package com.vrushil.subscription_management.request;

import com.vrushil.subscription_management.entity.BillingCycle;
import com.vrushil.subscription_management.entity.PlanType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlanRequest {

    @NotNull(message = "App ID is required")
    private Long appId;

    @NotNull(message = "Billing cycle is required")
    private BillingCycle billingCycle;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    private String features;

    @NotNull(message = "Plan type is required")
    private PlanType planType;
}

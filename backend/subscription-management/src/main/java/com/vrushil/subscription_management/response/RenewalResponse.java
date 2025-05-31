package com.vrushil.subscription_management.response;

import com.vrushil.subscription_management.entity.BillingCycle;
import com.vrushil.subscription_management.entity.PlanType;
import com.vrushil.subscription_management.entity.TransactionStatus;
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
public class RenewalResponse {
    private Long renewalId;
    private Long planId;
    private Long appId;
    private Long userId;
    private Long subscriptionId;
    private String appName;
    private String website;
    private LocalDate renewalDate;
    private PlanType planType;
    private BigDecimal price;
    private boolean approved;
    private TransactionStatus transactionStatus;
    private BillingCycle billingCycle;
    private String features;
}

package com.vrushil.subscription_management.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;


@Data
public class RenewalRequest {

    @NotNull
    private Long subscriptionId;
    @NotNull
    private Long planId;
    private LocalDate renewalDate;
}

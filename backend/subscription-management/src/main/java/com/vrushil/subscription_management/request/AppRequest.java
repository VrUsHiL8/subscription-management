package com.vrushil.subscription_management.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppRequest {
    @NotNull(message = "App name is required")
    private String appName;
    private String logoUrl;
    private String website;
    private Long categoryId;

}

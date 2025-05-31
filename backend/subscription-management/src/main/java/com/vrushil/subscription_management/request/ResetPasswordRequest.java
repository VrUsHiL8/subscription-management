package com.vrushil.subscription_management.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotNull
    private String token;
    @NotNull
    private String newPassword;

}

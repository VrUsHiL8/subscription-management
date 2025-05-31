package com.vrushil.subscription_management.response;

import com.vrushil.subscription_management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String loginMethod;   // "EMAIL" or "PHONE"
    private Role role;
    private String message;
}
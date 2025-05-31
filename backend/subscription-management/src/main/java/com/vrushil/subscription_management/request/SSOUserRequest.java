package com.vrushil.subscription_management.request;

import lombok.Data;

@Data
public class SSOUserRequest {
    private String email;
    private String name;
    private String phone;
    private String provider; // e.g., Google, Facebook
    private String providerId;
}

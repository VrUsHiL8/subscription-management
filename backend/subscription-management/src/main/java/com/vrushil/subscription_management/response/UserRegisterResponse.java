package com.vrushil.subscription_management.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterResponse {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String loginMethod; // "EMAIL" or "PHONE"
}
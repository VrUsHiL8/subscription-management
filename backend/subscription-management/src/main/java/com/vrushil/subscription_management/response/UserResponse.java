package com.vrushil.subscription_management.response;

import com.vrushil.subscription_management.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String loginMethod;  // "EMAIL" or "PHONE"
    private String ssoProvider;  // "Google", "Facebook", etc. (nullable if not SSO)

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.role = user.getRole().name();
        // Determine login method and SSO provider
        if (Boolean.TRUE.equals(user.getIsOAuthUser())) { // ✅ Handle Boolean correctly
            this.loginMethod = "OAuth - " + (user.getProvider() != null ? user.getProvider() : "Unknown");
            this.ssoProvider = user.getProvider();
        } else {
            this.loginMethod = "Email/Password";
            this.ssoProvider = null;
        }
    }
}

package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.entity.Role;
import com.vrushil.subscription_management.request.*;
import com.vrushil.subscription_management.response.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AuthService {
    ApiResponse<UserResponse> register(UserRegisterRequest userRegisterRequest);
    ApiResponse<AuthResponse> login(LoginRequest request);
    ApiResponse<AuthResponse> handleOAuth2Login(OAuth2User oAuth2User, Role role);
    ApiResponse<UpdateRoleResponse> updateUserRole(Long id, UpdateRoleRequest roleRequest);
    ApiResponse<AuthResponse>logout(LogoutRequest logoutRequest);
}

package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.request.ResetPasswordRequest;
import com.vrushil.subscription_management.request.UserRegisterRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    ApiResponse<UserResponse> getUserById(Long id);

    ApiResponse<Page<UserResponse>> getAllUsers(String search,Pageable pageable);

    ApiResponse<UserResponse> updateUser(Long id, UserRegisterRequest updateUser);

    ApiResponse<Void> deleteUser(Long id);

    ApiResponse<Void> sendResetLink(String email);

    ApiResponse<Void> resetPassword(ResetPasswordRequest request);
}

package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.RefreshTokenResponse;

public interface RefreshTokenService {
    ApiResponse<RefreshTokenResponse> refreshToken(String refreshToken);
}

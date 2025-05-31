package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.entity.RefreshToken;
import com.vrushil.subscription_management.entity.User;
import com.vrushil.subscription_management.repository.RefreshTokenRepository;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.RefreshTokenResponse;
import com.vrushil.subscription_management.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Value("${app.jwt.refreshTokenExpirationMs}")
    private Long refreshTokenDurationMs;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
                                   JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public ApiResponse<RefreshTokenResponse> refreshToken(String oldRefreshToken) {
        try {
            // Find existing refresh token in DB
            Optional<RefreshToken> storedTokenOpt = refreshTokenRepository.findByToken(oldRefreshToken);
            if (storedTokenOpt.isEmpty()) {
                return new ApiResponse<>("ERROR", "Refresh token not found", null);
            }

            RefreshToken storedToken = storedTokenOpt.get();

            // Verify expiration
            if (storedToken.getExpiryDate().isBefore(Instant.now())) {
                refreshTokenRepository.delete(storedToken);
                return new ApiResponse<>("ERROR", "Refresh token expired. Please sign in again.", null);
            }

            // Extract user details
            User user = storedToken.getUser();
            String newAccessToken = jwtUtil.generateToken(user);
            String newRefreshToken = UUID.randomUUID().toString();
            Instant newExpiryDate = Instant.now().plusMillis(refreshTokenDurationMs);

            // ✅ Instead of deleting, update the existing token
            storedToken.setToken(newRefreshToken);
            storedToken.setExpiryDate(newExpiryDate);
            refreshTokenRepository.save(storedToken);

            RefreshTokenResponse response = new RefreshTokenResponse(newAccessToken, newRefreshToken, newExpiryDate);
            return new ApiResponse<>("SUCCESS", "Token refreshed successfully", response);
        } catch (Exception e) {
            return new ApiResponse<>("ERROR", "Token refresh failed: " + e.getMessage(), null);
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired. Please sign in again.");
        }
        return token;
    }
}

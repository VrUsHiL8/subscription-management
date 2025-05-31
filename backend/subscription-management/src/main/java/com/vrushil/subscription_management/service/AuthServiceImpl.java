package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.entity.RefreshToken;
import com.vrushil.subscription_management.entity.Role;
import com.vrushil.subscription_management.entity.User;
import com.vrushil.subscription_management.exception.DuplicateUserException;
import com.vrushil.subscription_management.exception.UserNotFoundException;
import com.vrushil.subscription_management.repository.RefreshTokenRepository;
import com.vrushil.subscription_management.repository.UserRepository;
import com.vrushil.subscription_management.request.*;
import com.vrushil.subscription_management.response.*;
import com.vrushil.subscription_management.security.JwtUtil;
import com.vrushil.subscription_management.util.Mapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           JwtUtil jwtUtil,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ApiResponse<UserResponse> register(UserRegisterRequest userRegisterRequest) {
        if (userRepository.findByEmailOrPhone(userRegisterRequest.getEmail(), userRegisterRequest.getPhone()).isPresent()) {
            throw new DuplicateUserException("Email or phone number already exists");
        }

        User user = new User();
        user.setName(userRegisterRequest.getName());
        user.setEmail(userRegisterRequest.getEmail());
        user.setPhone(userRegisterRequest.getPhone());

        if (userRegisterRequest.getPassword() == null || userRegisterRequest.getPassword().isEmpty()) {
            user.setIsOAuthUser(true);
            user.setPassword("");
        } else {
            user.setIsOAuthUser(false);
            user.setPassword(bCryptPasswordEncoder.encode(userRegisterRequest.getPassword()));
        }

        user.setRole(Optional.ofNullable(userRegisterRequest.getRole()).orElse(Role.USER));
        userRepository.save(user);

        UserResponse userResponse = new UserResponse(user);
        return new ApiResponse<>("SUCCESS", "User registered successfully", userResponse);
    }

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByEmailOrPhone(request.getUsername(), request.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            String accessToken = jwtUtil.generateToken(user);
            String refreshToken = UUID.randomUUID().toString(); // Generate UUID refresh token

            saveRefreshToken(user, refreshToken);

            AuthResponse authResponse = new AuthResponse(
                    accessToken,
                    refreshToken,
                    "EMAIL",
                    user.getRole(),
                    "Login successful"
            );

            return new ApiResponse<>("SUCCESS", "Login successful", authResponse);
        } catch (Exception e) {
            return new ApiResponse<>("ERROR", "Invalid email or password", null);
        }
    }

    // OAuth2 Login
    public ApiResponse<AuthResponse> handleOAuth2Login(OAuth2User oAuth2User, Role role) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.getOrDefault("name", "Google User");
        Boolean emailVerified = (Boolean) attributes.getOrDefault("email_verified", false);
        String providerId = (String) attributes.getOrDefault("sub", ""); // Google Unique ID

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setIsOAuthUser(true);
            newUser.setProviderId(providerId);
            newUser.setEmailVerified(emailVerified);
            newUser.setPassword("");
            newUser.setRole(Role.USER);
            return userRepository.save(newUser);
        });

        String accessToken = jwtUtil.generateTokenForOAuth2(user.getUserId(), email, user.getRole().name());
        String refreshToken = UUID.randomUUID().toString(); // Generate UUID refresh token

        saveRefreshToken(user, refreshToken);

        AuthResponse authResponse = new AuthResponse(accessToken, refreshToken, "OAUTH", role, "OAuth2 Login successful");
        return new ApiResponse<>("SUCCESS", "OAuth2 login successful", authResponse);
    }

    @Transactional
    public void saveRefreshToken(User user, String refreshToken) {
        Instant expiryDate = Instant.now().plusSeconds(7 * 24 * 60 * 60); // 7 days expiry

        Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUser(user);

        if (existingTokenOpt.isPresent()) {
            RefreshToken token = existingTokenOpt.get();
            token.setToken(refreshToken);
            token.setExpiryDate(expiryDate);
            refreshTokenRepository.save(token);
        } else {
            RefreshToken token = new RefreshToken();
            token.setToken(refreshToken);
            token.setUser(user);
            token.setExpiryDate(expiryDate);
            refreshTokenRepository.save(token);
        }
    }

    @Override
    public ApiResponse<UpdateRoleResponse> updateUserRole(Long id, UpdateRoleRequest roleRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + id));

        user.setRole(roleRequest.getRole());
        User updatedUser = userRepository.save(user);
        return new ApiResponse<>("SUCCESS", "User role updated successfully", Mapper.toUpdateRoleResponse(updatedUser));
    }

    @Override
    public ApiResponse<AuthResponse> logout(LogoutRequest logoutRequest) {
        Optional<User> user = userRepository.findByEmail(logoutRequest.getEmail());

        if (user.isPresent()) {
            refreshTokenRepository.deleteByUser(user.get());
            log.info("✅ User logged out successfully.");

            return new ApiResponse<>("SUCCESS", "User logged out successfully.", null);
        }
        return new ApiResponse<>("Error", "User not found", null);
    }
}

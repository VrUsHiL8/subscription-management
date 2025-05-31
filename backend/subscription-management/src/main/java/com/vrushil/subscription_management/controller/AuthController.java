package com.vrushil.subscription_management.controller;


import com.vrushil.subscription_management.entity.Role;
import com.vrushil.subscription_management.request.*;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.AuthResponse;
import com.vrushil.subscription_management.response.RefreshTokenResponse;
import com.vrushil.subscription_management.response.UserResponse;
import com.vrushil.subscription_management.service.AuthService;
import com.vrushil.subscription_management.service.RefreshTokenService;
import com.vrushil.subscription_management.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public  AuthController(AuthService authService,
                           UserService userService,
                           RefreshTokenService refreshTokenService){
        this.authService=authService;
        this.userService=userService;
        this.refreshTokenService=refreshTokenService;
    }

    //Register User
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody UserRegisterRequest userRegisterRequest){
        ApiResponse<UserResponse> response = authService.register(userRegisterRequest);
        return ResponseEntity.ok(response);
    }

    //Login User
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request){
        ApiResponse<AuthResponse> response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/oauth2/callback")
    public ResponseEntity<ApiResponse<AuthResponse>> oauth2Login(@AuthenticationPrincipal OAuth2User oAuth2User, @RequestBody Role role) {
        ApiResponse<AuthResponse> response = authService.handleOAuth2Login(oAuth2User,role);
        return ResponseEntity.ok(response);
    }

    // Refresh Token Endpoint
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        ApiResponse<RefreshTokenResponse> response = refreshTokenService.refreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(response.getStatus().equals("SUCCESS") ? 200 : 401).body(response);
    }

    // Reset Password link
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> sendResetLink(@RequestParam String email){
        return ResponseEntity.ok(userService.sendResetLink(email));
    }

    // Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @RequestBody ResetPasswordRequest request){
        return  ResponseEntity.ok(userService.resetPassword(request));
    }

    // Logout User
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<AuthResponse>> logout(@RequestBody LogoutRequest logoutRequest) {
        ApiResponse<AuthResponse> response = authService.logout(logoutRequest);
        return ResponseEntity.ok(response);
    }

}

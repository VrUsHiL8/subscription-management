package com.vrushil.subscription_management.controller;

import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.AppResponse;
import com.vrushil.subscription_management.service.AppService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/apps")
public class AppController {

    private final AppService appService;

    public AppController(AppService appService) {
        this.appService=appService;
    }

    //Get App by CategoryId
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<Page<AppResponse>>> getAppByCategoryId(@PathVariable Long categoryId,Pageable pageable) {
        ApiResponse<Page<AppResponse>> response = appService.getAppByCategoryId(pageable,categoryId);
        return ResponseEntity.ok(response);
    }
    // Get App by ID
    @GetMapping("/{appId}")
    public ResponseEntity<ApiResponse<AppResponse>> getAppById(@PathVariable Long appId, @AuthenticationPrincipal OAuth2User principal) {
        ApiResponse<AppResponse> response = appService.getAppById(appId);
        return ResponseEntity.ok(response);
    }

    // Get All Apps
    @GetMapping("/all-apps")
    public ResponseEntity<ApiResponse<Page<AppResponse>>> getAllApps(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        ApiResponse<Page<AppResponse>> response = appService.getAllApps(search,pageable);
        return ResponseEntity.ok(response);
    }
}

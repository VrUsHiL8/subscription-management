package com.vrushil.subscription_management.controller;


import com.vrushil.subscription_management.request.UserRegisterRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.UserResponse;
import com.vrushil.subscription_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {

   private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

     // Get User By id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id){
        ApiResponse<UserResponse> response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    // Update User
    @PutMapping("/update/{id}")
    public  ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id, @RequestBody UserRegisterRequest updateUser){
        ApiResponse<UserResponse> response = userService.updateUser(id, updateUser);
        return ResponseEntity.ok(response);
    }

    // Delete User
    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id){
        ApiResponse<Void> response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }
}

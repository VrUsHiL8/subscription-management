package com.vrushil.subscription_management.request;

import com.vrushil.subscription_management.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRegisterRequest {

     @NotNull
     private String name;

     @NotNull
     @Email(message = "Email should be valid")
     private String email;

     @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits.")
     private String phone;

     @NotNull
     @Pattern(
             regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
             message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character, and be at least 8 characters long."
     )
     private String password;

     private Role role;
}

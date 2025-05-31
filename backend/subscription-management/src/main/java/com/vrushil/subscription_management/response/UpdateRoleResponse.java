package com.vrushil.subscription_management.response;

import com.vrushil.subscription_management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRoleResponse {
    private Long userId;
    private Role role;
}

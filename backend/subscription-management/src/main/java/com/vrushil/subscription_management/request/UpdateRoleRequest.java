package com.vrushil.subscription_management.request;

import com.vrushil.subscription_management.entity.Role;
import lombok.Data;

@Data
public class UpdateRoleRequest {
    private Role role;
}

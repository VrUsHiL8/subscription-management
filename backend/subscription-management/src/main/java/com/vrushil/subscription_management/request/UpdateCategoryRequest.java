package com.vrushil.subscription_management.request;

import lombok.Data;

@Data
public class UpdateCategoryRequest {
    private String categoryType;
    private String description;
}
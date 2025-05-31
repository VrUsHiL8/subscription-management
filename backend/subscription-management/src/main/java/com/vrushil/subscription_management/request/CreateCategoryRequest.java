package com.vrushil.subscription_management.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCategoryRequest {
    @NotNull(message = "Category type is required")
    private String categoryType;
    private String description;
}

package com.vrushil.subscription_management.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppResponse {

    private Long appId;
    private String appName;
    private String logoUrl;
    private String website;
    private Long categoryId;
    private String categoryType;

}

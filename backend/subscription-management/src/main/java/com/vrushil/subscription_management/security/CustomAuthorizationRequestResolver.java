package com.vrushil.subscription_management.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo, String baseUri) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, baseUri);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
        return customizeState(request, req);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientId) {
        OAuth2AuthorizationRequest req = defaultResolver.resolve(request, clientId);
        return customizeState(request, req);
    }

    private OAuth2AuthorizationRequest customizeState(HttpServletRequest request, OAuth2AuthorizationRequest req) {
        if (req == null) return null;

        String state = request.getParameter("state");
        if (state != null) {
            return OAuth2AuthorizationRequest.from(req)
                    .state(state)
                    .build();
        }
        return req;
    }
}

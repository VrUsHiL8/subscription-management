package com.vrushil.subscription_management.security;

import com.vrushil.subscription_management.entity.Role;
import com.vrushil.subscription_management.entity.User;
import com.vrushil.subscription_management.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public CustomOAuth2SuccessHandler(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        String email = oidcUser.getEmail();
        String name = oidcUser.getAttribute("name");
        Boolean emailVerified = oidcUser.getAttribute("email_verified");
        String providerId = oidcUser.getAttribute("sub");

        // Check if user already exists, else create a new one
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name != null ? name : "Google User");
            newUser.setIsOAuthUser(true);
            newUser.setProviderId(providerId);
            newUser.setEmailVerified(emailVerified != null);
            newUser.setPassword("");
            newUser.setRole(Role.USER);
            return userRepository.save(newUser);
        });

        String jwtToken = jwtUtil.generateTokenForOAuth2(user.getUserId(), user.getEmail(), user.getRole().name());

        String state = request.getParameter("state");

        String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:4200/login")
                .queryParam("token", jwtToken)
                .queryParam("state", state != null ? state : "/dashboard")
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}

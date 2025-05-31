package com.vrushil.subscription_management.security;

import com.vrushil.subscription_management.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.accessTokenExpirationMs}")
    private long accessTokenExpirationMs;

    private Key getSigningKey() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid secret key: Ensure it's a valid Base64 string and at least 32 bytes long.");
        }
    }

    // Generate Access Token (for regular login)
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId());
        claims.put("role", user.getRole());
        claims.put("email", user.getEmail());
        return createToken(claims, user.getEmail(), accessTokenExpirationMs);
    }

    // Generate Access Token (for OAuth2 login)
    public String generateTokenForOAuth2(Long id, String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("role", role);
        claims.put("email", email);
        return createToken(claims, email, accessTokenExpirationMs);
    }

    // Create JWT Access Token
    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // Email as subject
                .setIssuedAt(new Date(currentTimeMillis)) // iat: Issued at
                .setExpiration(new Date(currentTimeMillis + expirationTime)) // exp: Expiration time
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Extract user ID from token
    public Optional<Long> extractUserId(String token) {
        return Optional.ofNullable(extractClaim(token, claims -> claims.get("id", Long.class)));
    }

    // Extract username (email) from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract role from token
    public Optional<String> extractRole(String token) {
        return Optional.ofNullable(extractClaim(token, claims -> claims.get("role", String.class)));
    }

    // Extract issued at (iat) time from token
    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    // Extract expiration (exp) time from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic method to extract any claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    // Validate Access Token
    public boolean validateToken(String token, UserDetails userDetails) {
        String usernameFromToken = extractUsername(token);
        return usernameFromToken != null && tokenIsValid(token) && usernameFromToken.equals(userDetails.getUsername());
    }

    private boolean tokenIsValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            log.error("JWT token expired: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}

package com.smartService.SmartServiceBookingAPIs.Services.Jwt;

import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.unauthorized;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.jwt.access-token-expire}")
    private long accessTokenExpire;

    @Value("${spring.jwt.refresh-token-expire}")
    private long refreshTokenExpire;

    private SecretKey signKey;

    @PostConstruct
    void init() {
        // HS512 requires at least 64 bytes
        this.signKey = Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // =========================
    // ACCESS TOKEN
    // =========================
    public String generateAccessToken(
            String userId,
            String email,
            String username,
            List<String> roles
    ) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("roles", roles);
        claims.put("type", "access");

        return buildToken(claims, email, accessTokenExpire);
    }

    // =========================
    // REFRESH TOKEN
    // =========================
    public String generateRefreshToken(
            String userId,
            String username
    ) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("type", "refresh");

        return buildToken(claims, username, refreshTokenExpire);
    }

    // =========================
    // TOKEN BUILDER
    // =========================
    private String buildToken(
            Map<String, Object> claims,
            String subject,
            long expiration
    ) {
        Date now = new Date();

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .signWith(signKey, Jwts.SIG.HS512)
                .compact();
    }

    // =========================
    // EXTRACT CLAIMS (HELPER)
    // =========================
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // =========================
    // EXTRACT USERNAME
    // =========================
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // =========================
    // EXTRACT USER ID
    // =========================
    public String extractUserId(String token) {
        return extractAllClaims(token).get("userId", String.class);
    }

    // =========================
    // EXTRACT EMAIL
    // =========================
    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    // =========================
    // EXTRACT ROLES
    // =========================
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }

    // =========================
    // TOKEN VALIDATION
    // =========================

    /**
     * Validates token with UserDetails (used in JWT filter)
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && isTokenExpired(token);
    }

    /**
     * Validates token without UserDetails (used for refresh token)
     */
    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) {
            logger.debug("Token validation failed: token is null or blank");
            return false;
        }

        try {
            return isTokenExpired(token);
        } catch (JwtException e) {
            logger.debug("Token validation failed: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            logger.debug("Token validation failed: illegal argument");
            return false;
        }
    }

    /**
     * Checks if token is expired
     */
    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return !expiration.before(new Date());
    }

    // =========================
    // GET CURRENT USER
    // =========================
    public Users getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw unauthorized("User not authenticated");
        }

        String email = authentication.getName(); // <-- comes from JWT subject

        return userRepository.findByEmail(email)
                .orElseThrow(() -> unauthorized("User not found"));
    }
}
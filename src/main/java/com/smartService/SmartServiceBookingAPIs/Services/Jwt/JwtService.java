package com.smartService.SmartServiceBookingAPIs.Services.Jwt;

import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
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
public class JwtService {

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

        return buildToken(claims, username, accessTokenExpire);
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
    // EXTRACT USERNAME
    // =========================
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(signKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // =========================
    // TOKEN VALIDATION
    // =========================
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(signKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
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

        Object principal = authentication.getPrincipal();

        if (principal instanceof Users user) {
            return user;
        }

        throw unauthorized("Invalid authentication principal");
    }
}

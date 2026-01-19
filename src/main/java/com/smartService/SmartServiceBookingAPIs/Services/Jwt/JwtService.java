package com.smartService.SmartServiceBookingAPIs.Services.Jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signKey, Jwts.SIG.HS512)
                .compact();
    }
}
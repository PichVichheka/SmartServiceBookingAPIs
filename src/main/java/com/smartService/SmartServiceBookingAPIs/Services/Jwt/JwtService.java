package com.smartService.SmartServiceBookingAPIs.Services.Jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

    @Value("${spring.jwt-secret}")
    private String jwtSecret;

    @Value("${spring.jwt-token-expire}")
    private long JwtTokenExpired; // milliseconds

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // 🔑 Generate simple token with only userId
    public String generateToken(
            String userId,
            String email,
            String username,
            List<String> roles
    ) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("type", "jwt");

        /*
         * =========================
         * 🔐 PROD FEATURE
         * =========================
         * Uncomment when you add RBAC (role-based access control)
         */
        if (roles != null && !roles.isEmpty()) {
            claims.put("roles", roles);
        }

        return buildToken(claims, username, JwtTokenExpired);
    }

    // =================
    // JWT BUILDER
    // =================
    private String buildToken(
            Map<String, Object> claims,
            String subject,
            long expiration
    ) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)

                /*
                 * =========================
                 * 🔐 PROD SECURITY
                 * =========================
                 * HS512 is good for prod IF secret is long enough
                 */
                .signWith(getSignKey(), Jwts.SIG.HS512)
                .compact();
    }
}

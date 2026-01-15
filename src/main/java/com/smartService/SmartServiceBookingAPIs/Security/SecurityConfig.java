package com.smartService.SmartServiceBookingAPIs.Security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    /**
     * Main security filter chain.
     * This defines how Spring Security handles:
     * - CORS
     * - CSRF
     * - Session management
     * - Endpoint authorization
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 1️⃣ Enable CORS using our configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2️⃣ Disable CSRF because this is a REST API (we use JWT, not forms)
                .csrf(AbstractHttpConfigurer::disable)

                // 3️⃣ Set session management to stateless
                // No session is stored on the server; each request must include authentication
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4️⃣ Define endpoint access rules
                .authorizeHttpRequests(auth -> auth

                        // Public endpoints (login, register, etc.) accessible by anyone
                        .requestMatchers("/auth/**").permitAll()

                        // Admin-only endpoints
                        .requestMatchers("/admin/**").hasRole("admin")

                        // User and Admin endpoints
                        .requestMatchers("/users/**").hasRole("admin")

                        // Any other request requires authentication
                                .anyRequest().permitAll()
//                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * Password encoder bean.
     * - BCrypt is a strong hashing algorithm
     * - Always hash passwords before saving to the database
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager bean.
     * - Used to authenticate username/password during login
     * - Spring automatically provides this based on UserDetailsService
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * CORS configuration bean
     * - Defines which front-end URLs can access the API
     * - Defines allowed HTTP methods and headers
     * - Allows credentials like cookies or Authorization headers
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 1️⃣ Allow requests only from your frontend
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // change to your frontend URL

        // 2️⃣ Allow HTTP methods used by your API
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3️⃣Allow all headers (e.g., Content-Type, Authorization)
        configuration.setAllowedHeaders(List.of("*"));

        // 4️⃣ Allow credentials (needed for JWT or cookies)
        configuration.setAllowCredentials(true);

        // Register this configuration for all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}


package com.smartService.SmartServiceBookingAPIs.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

 user
/**
 * Security configuration for the Smart Service Booking API.
 * Configures JWT-based stateless authentication, CORS, and endpoint authorization.
 */

 develop
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

user

    private final JwtAuthFilter jwtAuthFilter;

develop
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Stateless API → no CSRF
                .csrf(AbstractHttpConfigurer::disable)

 user
                // Stateless session management

                // No session, JWT only
develop
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
 user
                        .requestMatchers("/auth/**").permitAll()

 develop
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // Swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

 user
                        // Admin-only endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Category create (admin only)
                        .requestMatchers("/api/categories/**").hasRole("ADMIN")

                        // Everything else requires authentication
//                        .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .build();
    }

    // BCrypt password encoder

                        // Admin endpoints
                        .requestMatchers("/api/admin/**").hasRole("admin")

                        // Provider endpoints
                        .requestMatchers("/api/provider").hasRole("provider")


                        // Permit all for development
//                        .anyRequest().permitAll()
                        // Everything else requires auth
                        .anyRequest().authenticated()
                )

                // JWT filter BEFORE UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

 develop
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

  user
    // Expose AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // CORS configuration

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

develop
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

user
        // Allowed origins (update for production)
 develop
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:5173"
        ));

 user
        // Allowed HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
 develop

        configuration.setAllowedHeaders(List.of("*"));
 user

        // Allow credentials (cookies, auth headers)
        configuration.setAllowCredentials(true);

        // Cache preflight for 1 hour
        configuration.setMaxAge(3600L);

        // Apply CORS config to all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
 develop
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

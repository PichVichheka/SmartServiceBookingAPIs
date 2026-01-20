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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // CORS configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Disable CSRF for stateless JWT authentication
                .csrf(AbstractHttpConfigurer::disable)

                // Stateless session management
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Exception handling for JWT authentication and authorization
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                                // Public endpoints - no authentication required
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()

                                // Swagger/OpenAPI documentation - public access
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                                // ========== PRODUCTION SECURITY - UNCOMMENT BEFORE DEPLOYMENT ==========
                                // Role-based access control
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/api/provider/**").hasRole("PROVIDER")
//                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
//
//                        // All other endpoints require authentication
//                        .anyRequest().authenticated()
                                // ========================================================================

                                // ========== DEVELOPMENT ONLY - COMMENT OUT IN PRODUCTION ==========
                                .anyRequest().permitAll()
                        // ==================================================================
                )

                // Add JWT filter before the standard authentication filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ========== DEVELOPMENT ONLY - UPDATE FOR PRODUCTION ==========
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",    // React dev server
                "http://localhost:5173"     // Vite dev server
                // PRODUCTION: Replace with actual frontend domain(s)
                // "https://yourdomain.com",
                // "https://www.yourdomain.com"
        ));
        // ==============================================================

        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
package com.smartService.SmartServiceBookingAPIs.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse<String> body = new ApiResponse<>(false, "Authentication token is missing or invalid", null);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}

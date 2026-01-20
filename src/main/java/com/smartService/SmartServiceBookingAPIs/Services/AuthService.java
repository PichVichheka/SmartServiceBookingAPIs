package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.AuthRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.AuthResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.RefreshTokenResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.RegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    RefreshTokenResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    );

    RegisterResponse register(
            RegisterRequest request,
            HttpServletResponse response
    );

    AuthResponse login(
            AuthRequest request,
            HttpServletResponse response
    );
}

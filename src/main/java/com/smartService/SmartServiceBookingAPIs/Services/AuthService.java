package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.AuthRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ApiResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.RegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request, HttpServletResponse response);

    AuthRequest login(AuthRequest request,
                      HttpServletResponse response);

    ApiResponse<Object> logout(HttpServletRequest request, HttpServletResponse response);
}

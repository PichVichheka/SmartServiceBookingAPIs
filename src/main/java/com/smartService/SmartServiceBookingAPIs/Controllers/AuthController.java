package com.smartService.SmartServiceBookingAPIs.Controllers;

import com.smartService.SmartServiceBookingAPIs.DTO.request.AuthRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.AuthResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.RefreshTokenResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.RegisterResponse;
import com.smartService.SmartServiceBookingAPIs.Services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        RegisterResponse registerResponse =
                authService.register(request, response);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest request,
            HttpServletResponse response
    ) {
        AuthResponse authResponse =
                authService.login(request, response);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        RefreshTokenResponse refreshTokenResponse =
                authService.refreshToken(request, response);

        return ResponseEntity.ok(refreshTokenResponse);
    }
}

package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.AuthRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterDeviceRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.*;
import com.smartService.SmartServiceBookingAPIs.Entity.Roles;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.RoleRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import com.smartService.SmartServiceBookingAPIs.Services.AuthService;
//import com.smartService.SmartServiceBookingAPIs.Services.DeviceTrackingService;
import com.smartService.SmartServiceBookingAPIs.Services.Jwt.JwtService;
import com.smartService.SmartServiceBookingAPIs.Services.UserDeviceService;
import com.smartService.SmartServiceBookingAPIs.Utils.CookieHelper;
import com.smartService.SmartServiceBookingAPIs.Utils.HelperFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import ua_parser.Client;


import java.util.List;
import java.util.Set;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.*;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final CookieHelper cookieHelper;
    private final AuthenticationManager authenticationManager;
    private final HelperFunction helperFunction;
//    private final DeviceTrackingService deviceTrackingService;
    private final UserDeviceService userDeviceService;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletRequest httpRequest;

    @Override
    public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        // ============================
        // Extract refresh token from cookie
        // ============================
        String refreshToken = cookieHelper.getCookieValue(request, "refresh_token");

        if (refreshToken == null || refreshToken.isBlank()) {
            throw unauthorized("Refresh token is missing.");
        }

        // ============================
        // Validate refresh token
        // ============================
        if (!jwtService.validateToken(refreshToken)) {
            throw unauthorized("Invalid or expired refresh token.");
        }

        // ============================
        // Extract user info from refresh token
        // ============================
        String userId = jwtService.extractUserId(refreshToken);
        String username = jwtService.extractUsername(refreshToken);

        // ============================
        // Load user from database
        // ============================
        Users user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> notFound("User not found."));

        // Verify the username matches (additional security check)
        if (!user.getUsername().equals(username)) {
            throw unauthorized("Invalid refresh token.");
        }

        // ============================
        // Extract roles for new access token
        // ============================
        List<String> roles = user.getRoles()
                .stream()
                .map(Roles::getName)
                .toList();

        // ============================
        // Generate new tokens
        // ============================
        String newAccessToken = jwtService.generateAccessToken(
                user.getId().toString(),
                user.getEmail(),
                user.getUsername(),
                roles
        );

        String newRefreshToken = jwtService.generateRefreshToken(
                user.getId().toString(),
                user.getUsername()
        );

        // ============================
        // Set new tokens in cookies
        // ============================
        cookieHelper.setAuthCookie(response, "access_token", newAccessToken, 15 * 60);           // 15 min
        cookieHelper.setAuthCookie(response, "refresh_token", newRefreshToken, 7 * 24 * 60 * 60); // 7 days

        // ============================
        // Return lightweight response
        // ============================
        return new RefreshTokenResponse(
                true,
                "Token refreshed successfully.",
                newAccessToken
        );
    }

    @Override
    public AuthResponse register(RegisterRequest request, HttpServletResponse response) {

        // ============================
        // Validate input fields
        // ============================
        helperFunction.validateRegister(request);

        // ============================
        // Map request to user entity (MANUAL MAPPING)
        // ============================
        Users user = new Users();
        user.setFullname(request.getFullname());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // ============================
        // Assign default role (customer)
        // ============================
        Roles userRole = roleRepository.findByName("customer")
                .orElseThrow(() -> notFound("Default role not found."));
        user.setRoles(Set.of(userRole));

        // ============================
        // Save user to DB
        // ============================
        Users savedUser = userRepository.save(user);

        // ============================
        // Extract role names for JWT
        // ============================
        List<String> roles = savedUser.getRoles()
                .stream()
                .map(Roles::getName)
                .toList();

        // ============================
        // Generate JWT tokens
        // ============================
        String accessToken = jwtService.generateAccessToken(
                savedUser.getId().toString(),
                savedUser.getEmail(),
                savedUser.getUsername(),
                roles
        );

        String refreshToken = jwtService.generateRefreshToken(
                savedUser.getId().toString(),
                savedUser.getUsername()
        );

        // ============================
        // Set tokens as HTTP-only cookies
        // ============================
        cookieHelper.setAuthCookie(response, "access_token", accessToken, 15 * 60);           // 15 min
        cookieHelper.setAuthCookie(response, "refresh_token", refreshToken, 7 * 24 * 60 * 60); // 7 days

        // ============================
        // Map user entity to response DTO (MANUAL MAPPING)
        // ============================
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setFullname(savedUser.getFullname());
        userResponse.setUsername(savedUser.getUsername());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPhone(savedUser.getPhone());
        userResponse.setRoles(roles);

        return new AuthResponse(
                201,
                "Registration successful.",
                accessToken,
                userResponse
        );
    }



    @Override
    public AuthResponse login(AuthRequest request, HttpServletResponse response) {

        // ============================
        // Validate input fields
        // ============================
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw badRequest("Email is required.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw badRequest("Password is required.");
        }

        // Validate email format
        helperFunction.validateEmailFormat(request.getEmail());

        // ============================
        // Authenticate using AuthenticationManager
        // ============================
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // ============================
        // Extract authenticated user
        // ============================
        Users principal = (Users) authentication.getPrincipal();

        // Load your Users entity from DB
        Users user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> validation("User not found after authentication."));

        // ============================
        // Track user device (Add later)
        // ============================


        // ============================
        // Extract roles for JWT
        // ============================
        List<String> roles = user.getRoles()
                .stream()
                .map(Roles::getName)
                .toList();

        // ============================
        // Generate JWT tokens
        // ============================
        String accessToken = jwtService.generateAccessToken(
                user.getId().toString(),
                user.getEmail(),
                user.getUsername(),
                roles
        );

        String refreshToken = jwtService.generateRefreshToken(
                user.getId().toString(),
                user.getUsername()
        );

        // ============================
        // Set tokens in HTTP-only cookies
        // ============================
        cookieHelper.setAuthCookie(response, "access_token", accessToken, 15 * 60);           // 15 min
        cookieHelper.setAuthCookie(response, "refresh_token", refreshToken, 7 * 24 * 60 * 60); // 7 days

        // ============================
        // Map user entity to response DTO (MANUAL MAPPING)
        // ============================
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFullname(user.getFullname());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRoles(roles);

        return new AuthResponse(
                200,
                "Login successful.",
                accessToken,
                userResponse
        );
    }

    @Override
    public ApiResponse<Object> logout(HttpServletRequest request, HttpServletResponse response) {

        String access_token = cookieHelper.getCookieValue(request, "access_token");
        String refresh_token = cookieHelper.getCookieValue(request, "refresh_token");

        cookieHelper.clearAuthCookie(response, "access_token");
        cookieHelper.clearAuthCookie(response, "refresh_token");

        return new ApiResponse<>(true, "User logout successfully.");
    }
}
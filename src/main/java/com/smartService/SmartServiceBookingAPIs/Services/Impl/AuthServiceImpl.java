package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.AuthRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.AuthResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.RegisterResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.UserResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Roles;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Mapper.MapperFunction;
import com.smartService.SmartServiceBookingAPIs.Repositories.RoleRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import com.smartService.SmartServiceBookingAPIs.Services.AuthService;
import com.smartService.SmartServiceBookingAPIs.Services.Jwt.JwtService;
import com.smartService.SmartServiceBookingAPIs.Utils.CookieHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.notFound;
import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.validation;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final CookieHelper cookieHelper;
    private final MapperFunction mapperFunction;
    private final AuthenticationManager authenticationManager;

    @Override
    public RegisterResponse register(RegisterRequest request, HttpServletResponse response) {
        // Map request to user entity
        Users user = mapperFunction.toUsers(request);
        user.setPassword(Objects.requireNonNull(passwordEncoder.encode(request.getPassword())));

        // Assign default customer role
        Roles userRole = roleRepository.findByName("customer")
                .orElseThrow(() -> notFound("Default role not found."));

        user.setRoles(Set.of(userRole));

        // Save user to database
        Users savedUser = userRepository.save(user);

        // Extract role names for JWT
        List<String> roles = savedUser.getRoles()
                .stream()
                .map(Roles::getName)
                .toList();

        // Generate JWT tokens
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

        // Set tokens as HTTP-only cookies
        cookieHelper.setAuthCookie(response, "access_token", accessToken, 15 * 60);
        cookieHelper.setAuthCookie(response, "refresh_token", refreshToken, 7 * 24 * 60 * 60);

        UserResponse userResponse = mapperFunction.toUserResponse(savedUser);

        return new RegisterResponse(
                201,
                "Registration successful.",
                accessToken,
                userResponse
        );
    }


    @Override
    public AuthResponse login(AuthRequest request, HttpServletResponse response) {
        try {
            // Use AuthenticationManager to authenticate credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Extract the custom user details from authentication principal
            UserDetailsServiceImpl.CustomUserDetails userDetails =
                    (UserDetailsServiceImpl.CustomUserDetails) authentication.getPrincipal();

            // Get the actual Users entity from CustomUserDetails
            assert userDetails != null;
            Users user = userDetails.getUser();

            // Extract role names for JWT
            List<String> roles = user.getRoles()
                    .stream()
                    .map(Roles::getName)
                    .toList();

            // Generate JWT tokens
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

            // Set tokens as HTTP-only cookies
            cookieHelper.setAuthCookie(response, "access_token", accessToken, 15 * 60);
            cookieHelper.setAuthCookie(response, "refresh_token", refreshToken, 7 * 24 * 60 * 60);

            UserResponse userResponse = mapperFunction.toUserResponse(user);

            return new AuthResponse(
                    200,
                    "Login successful.",
                    accessToken,
                    userResponse
            );

        } catch (BadCredentialsException e) {
            throw validation("Invalid email or password. Please try again.");
        } catch (AuthenticationException e) {
            throw validation("Authentication failed: " + e.getMessage());
        }
    }
}
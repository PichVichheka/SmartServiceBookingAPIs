package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.AuthRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ApiResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.AuthResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.RegisterResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.UserResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Roles;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Mapper.MapperFunction;
import com.smartService.SmartServiceBookingAPIs.Repositories.RoleRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
//import com.smartService.SmartServiceBookingAPIs.Utils.CookieHelper;
import com.smartService.SmartServiceBookingAPIs.Services.AuthService;
import com.smartService.SmartServiceBookingAPIs.Services.Jwt.JwtService;
import com.smartService.SmartServiceBookingAPIs.Utils.HelperFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.notFound;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
//    private final CookieHelper cookieHelper;
    private final RoleRepository roleRepository;
    private final MapperFunction mapperFunction;
    private final HelperFunction helperFunction;


    @Override
    public RegisterResponse register(RegisterRequest request, HttpServletResponse response) {

        // ========================
        // VALIDATE REQUEST
        // ========================
        helperFunction.validateCreate(request);

        // MAP REQUEST TO ENTITY
        Users user = mapperFunction.toUsers(request);

        // ========================
        // SET PASSWORD ENCODER
        // ========================
        user.setPassword(Objects.requireNonNull(passwordEncoder.encode(request.getPassword())));

        // ========================
        // ASSIGN ROLE TO USER
        // ========================
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            List<Roles> roles = roleRepository.findAllById(request.getRoles());
            if (roles.size() != request.getRoles().size()) {
                throw notFound("Roles not found.");
            }
            user.setRoles(new HashSet<>(roles));
        } else {
            Roles defaultRole = roleRepository.findByName("customer")
                    .orElseThrow(() -> notFound("Default role not found."));
            user.setRoles(new HashSet<>(Set.of(defaultRole)));
        }

        // ========================
        // SAVE USER
        // ========================
        Users savedUser = userRepository.save(user);

        // ========================
        // MAP USER RESPONSE USING MAPSTRUCT
        // ========================
        UserResponse userResponse = mapperFunction.toUserResponse(savedUser);

        // ========================
        // GENERATE TOKEN
        // ========================
        String token = jwtService.generateToken(
                String.valueOf(savedUser.getId()),
                savedUser.getEmail(),
                savedUser.getUsername(),
                savedUser.getRoles().stream().map(Roles::getName).toList()
        );

        // ========================
        // RETURN FLAT RESPONSE
        // ========================
        return new RegisterResponse(201, "User registered successfully", token, userResponse);
    }

    @Override
    public AuthRequest login(AuthRequest request, HttpServletResponse response) {
        return null;
    }


    @Override
    public ApiResponse<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}

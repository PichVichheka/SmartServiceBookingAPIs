package com.smartService.SmartServiceBookingAPIs.Utils;

import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.UserCreateRequest;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import com.smartService.SmartServiceBookingAPIs.DTO.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.badRequest;

@Component
@RequiredArgsConstructor
public class HelperFunction {

    private final UserRepository userRepository;

    // =================
    // EMAIL FORMAT VALIDATOR
    // =================
    public void validateEmailFormat(String email) {
        if (email == null || !email.contains("@") || !email.endsWith(".com")) {
            throw badRequest("Email must contain '@' and end with '.com'");
        }
    }

    // =================
    // CREATE VALIDATION
    // =================
    public void validateRegister(RegisterRequest request) {

        String email = request.getEmail();
        String username = request.getUsername();

        // EMAIL REQUIRED
        if (email == null || email.isBlank()) {
            throw badRequest("Email is required.");
        }

        validateEmailFormat(email);

        // EMAIL ALREADY EXISTS
        if (userRepository.existsByEmail(email)) {
            throw badRequest("Email is already in use.");
        }

        // USERNAME REQUIRED
        if (username == null || username.isBlank()) {
            throw badRequest("Username is required.");
        }

        // USERNAME ALREADY EXISTS
        if (userRepository.existsByUsername(username)) {
            throw badRequest("Username is already in use.");
        }
    }

    public void validateCreate(UserCreateRequest request) {

        String email = request.getEmail();
        String username = request.getUsername();

        // EMAIL REQUIRED
        if (email == null || email.isBlank()) {
            throw badRequest("Email is required.");
        }

        validateEmailFormat(email);

        // EMAIL ALREADY EXISTS
        if (userRepository.existsByEmail(email)) {
            throw badRequest("Email is already in use.");
        }

        // USERNAME REQUIRED
        if (username == null || username.isBlank()) {
            throw badRequest("Username is required.");
        }

        // USERNAME ALREADY EXISTS
        if (userRepository.existsByUsername(username)) {
            throw badRequest("Username is already in use.");
        }
    }


    // =================
    // UPDATE VALIDATION (PROFILE ONLY)
    // =================
    public void validateUpdate(UserUpdateRequest request) {

        if (request.getFullname() == null || request.getFullname().isBlank()) {
            throw badRequest("Full name must not be empty.");
        }

        if (request.getPhone() == null || request.getPhone().isBlank()) {
            throw badRequest("Phone number must not be empty.");
        }
    }
}
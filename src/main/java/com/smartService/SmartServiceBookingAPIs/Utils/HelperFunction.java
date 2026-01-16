package com.smartService.SmartServiceBookingAPIs.Utils;

import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import com.smartService.SmartServiceBookingAPIs.DTO.request.UserUpdateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.UserCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.badRequest;

@Component
public class HelperFunction {

    @Autowired
    private UserRepository userRepository;

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
    public void validateCreate(UserCreateRequest request) {
        validateEmailFormat(request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw badRequest("Email is already in use.");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
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

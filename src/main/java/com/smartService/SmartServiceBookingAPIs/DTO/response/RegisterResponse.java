package com.smartService.SmartServiceBookingAPIs.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private int statusCode;
    private String message;
    private String token;
    private UserResponse user;
}


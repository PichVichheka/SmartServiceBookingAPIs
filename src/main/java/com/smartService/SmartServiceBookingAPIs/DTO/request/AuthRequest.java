package com.smartService.SmartServiceBookingAPIs.DTO.request;

import lombok.Data;
//import lombok.RequiredArgsConstructor;

@Data
//@RequiredArgsConstructor
public class AuthRequest {
    private String email;
    private String password;
}




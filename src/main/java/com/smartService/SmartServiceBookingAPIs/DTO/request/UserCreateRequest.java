package com.smartService.SmartServiceBookingAPIs.DTO.request;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String fullname;
    private String username;
    private String email;
    private String password;
    private String phone;
}

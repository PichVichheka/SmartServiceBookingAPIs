package com.smartService.SmartServiceBookingAPIs.DTO.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String fullname;
    private String username;
    private String email;
    private String password;
    private String phone;
    private List<Long> roles;
}

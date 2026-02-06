package com.smartService.SmartServiceBookingAPIs.DTO.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonPropertyOrder({
        "id",
        "fullname",
        "username",
        "phone",
        "email",
        "roles"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullname;
    private String username;
    private String email;
    private String phone;
    private List<String> roles;

    private UserDeviceResponse currentDevice;
}
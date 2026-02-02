package com.smartService.SmartServiceBookingAPIs.DTO.response;

import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;

@Data
//@RequiredArgsConstructor
//@NoArgsConstructor
public class UserDeviceResponse {
    private Long id;
    private String deviceName;
    private String os;
    private String browser;
    private boolean active;
}

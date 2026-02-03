package com.smartService.SmartServiceBookingAPIs.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

//import java.time.Instant;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;

@Data
//@RequiredArgsConstructor
//@NoArgsConstructor
@AllArgsConstructor
public class UserDeviceResponse {
    private Long id;
    private String deviceName;
    private String os;
    private String browser;
    private Instant lastSeenAt;
    private boolean active;
}

package com.smartService.SmartServiceBookingAPIs.DTO.response;

import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;

@Data
//@RequiredArgsConstructor
//@NoArgsConstructor
public class DeviceResponse {
    private String browser;
    private String os;
    private String device;
}

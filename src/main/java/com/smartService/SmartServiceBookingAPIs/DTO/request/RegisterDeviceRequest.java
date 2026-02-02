package com.smartService.SmartServiceBookingAPIs.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDeviceRequest {
    private String deviceId;
    private String deviceType;
    private String os;
    private String browser;
}

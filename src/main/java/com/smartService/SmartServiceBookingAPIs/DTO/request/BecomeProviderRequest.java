package com.smartService.SmartServiceBookingAPIs.DTO.request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class BecomeProviderRequest {
    private String serviceType;
    private String serviceDescription;
    private String experience;
//    private LocalTime preferredStartTime; // optional
//    private LocalTime preferredEndTime;   // optional
}

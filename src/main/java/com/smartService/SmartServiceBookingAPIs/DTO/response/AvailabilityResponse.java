package com.smartService.SmartServiceBookingAPIs.DTO.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AvailabilityResponse {

    private Long id;
    private Long providerId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean available;
}

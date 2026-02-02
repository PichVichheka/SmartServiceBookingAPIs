package com.smartService.SmartServiceBookingAPIs.DTO.request;


import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingReqest {

    private Long userId;
    private Long providerId;
    private Long serviceId;
    private Long availabilityId;
    private LocalDate bookingDate;
}

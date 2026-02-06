package com.smartService.SmartServiceBookingAPIs.DTO.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class BookingResponse {

    private Long id;
    private Long userId;
    private Long providerId;
    private Long serviceId;
    private Long availabilityId;
    private LocalDate bookingDate;
    private BigDecimal totalPrice;
    private String status;


}

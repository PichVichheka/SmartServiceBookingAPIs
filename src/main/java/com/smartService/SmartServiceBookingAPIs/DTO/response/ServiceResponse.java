package com.smartService.SmartServiceBookingAPIs.DTO.response;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {

    private Long id;
    private String serviceType;
    private String serviceDescription;
    private String serviceOffer;
    private BigDecimal price;
    private String priceUnit;

    private Long providerId;
    private String providerName;
}


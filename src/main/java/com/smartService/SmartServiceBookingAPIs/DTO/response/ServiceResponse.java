package com.smartService.SmartServiceBookingAPIs.DTO.response;

import com.smartService.SmartServiceBookingAPIs.Entity.Services;
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

    private ServiceResponse mapToResponse(Services service) {
        return ServiceResponse.builder()
                .id(service.getId())
                .serviceType(service.getServiceType())
                .serviceDescription(service.getServiceDescription())
                .serviceOffer(service.getServiceOffer())
                .price(service.getPrice())
                .priceUnit(service.getPriceUnit())
                .providerId(
                        service.getProvider() != null ? service.getProvider().getId() : null
                )
                .providerName(
                        service.getProvider() != null ? service.getProvider().getName() : null
                )
                .build();
    }
}


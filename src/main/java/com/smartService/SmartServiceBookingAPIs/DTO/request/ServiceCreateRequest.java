package com.smartService.SmartServiceBookingAPIs.DTO.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ServiceCreateRequest {

    private String serviceType;
    private String serviceDescription;
    private String serviceOffer;
    private BigDecimal price;
    private String priceUnit;

}

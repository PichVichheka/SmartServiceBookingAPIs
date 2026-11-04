package com.smartService.SmartServiceBookingAPIs.DTO.response;

import com.smartService.SmartServiceBookingAPIs.Entity.RequestStatus;
import lombok.Data;

@Data
public class ProviderRequestResponse {
    private Long id;
    private String serviceType;
    private String serviceDescription;
    private String experience;
    private RequestStatus status;
    private UserSummaryResponse user;
}


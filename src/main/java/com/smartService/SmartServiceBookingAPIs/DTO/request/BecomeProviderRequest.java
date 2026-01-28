package com.smartService.SmartServiceBookingAPIs.DTO.request;

import lombok.Data;


@Data
public class BecomeProviderRequest {
    private String serviceType;
    private String serviceDescription;
    private String experience;
}

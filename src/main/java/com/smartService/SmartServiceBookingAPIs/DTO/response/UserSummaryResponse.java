package com.smartService.SmartServiceBookingAPIs.DTO.response;

import lombok.Data;

@Data
public class UserSummaryResponse {
    private Long id;
    private String fullname;
    private String email;
    private String phone;
}

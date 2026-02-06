package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.response.UserDeviceResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import ua_parser.Client;

public interface DeviceTrackingService {

    UserDeviceResponse trackUserDevice(Users users, HttpServletRequest request);
//    Client trackLogin(Users user, HttpServletRequest request);
}

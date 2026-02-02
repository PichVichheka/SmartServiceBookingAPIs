package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import ua_parser.Client;

public interface DeviceTrackingService {
    Client trackLogin(Users user, HttpServletRequest request);
}

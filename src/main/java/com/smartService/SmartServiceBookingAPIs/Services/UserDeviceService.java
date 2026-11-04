package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterDeviceRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.UserDeviceResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;

import java.util.List;

public interface UserDeviceService {

    UserDeviceResponse registerDevice(
            Users users,
            RegisterDeviceRequest request,
            String ipAddress
    );

    List<UserDeviceResponse> getMyDevices(Users users);
}

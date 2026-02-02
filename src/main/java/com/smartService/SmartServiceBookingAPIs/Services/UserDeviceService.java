package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterDeviceRequest;
import com.smartService.SmartServiceBookingAPIs.Entity.UserDevice;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;

public interface UserDeviceService {

    UserDevice registerDevice(
            Users users,
            RegisterDeviceRequest request,
            String ipAddress
    );
}

package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.Entity.UserDevice;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;

public interface NotificationService {

    void sendNewDeviceAlert(Users users, UserDevice userDevice);
}

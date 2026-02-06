package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.Entity.UserDevice;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendNewDeviceAlert(Users users, UserDevice userDevice) {

        log.warn(
                "New Device Login detected | userId={} | deviceId={} | browser={} | ip={}",
                users.getId(),
                userDevice.getDeviceName(),
                userDevice.getOs(),
                userDevice.getBrowser(),
                userDevice.getIpAddress()
        );
    }
}

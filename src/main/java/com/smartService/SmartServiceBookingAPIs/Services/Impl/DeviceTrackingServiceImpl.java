package com.smartService.SmartServiceBookingAPIs.Services.Impl;


import com.smartService.SmartServiceBookingAPIs.DTO.response.UserDeviceResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.UserDevice;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserDeviceRepository;
import com.smartService.SmartServiceBookingAPIs.Services.DeviceInfoService;
import com.smartService.SmartServiceBookingAPIs.Services.DeviceTrackingService;
import com.smartService.SmartServiceBookingAPIs.Services.NotificationService;
import com.smartService.SmartServiceBookingAPIs.Utils.DeviceUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua_parser.Client;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DeviceTrackingServiceImpl implements DeviceTrackingService {

    private final UserDeviceRepository userDeviceRepository;
    private final DeviceInfoService deviceInfoService;
    private final NotificationService notificationService;

    private String extractIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return forwarded != null ? forwarded.split(",")[0].trim() : request.getRemoteAddr();
    }

    @Override
    public UserDeviceResponse trackUserDevice(Users users, HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");
        Client client = deviceInfoService.parse(userAgent);
        String ip = extractIp(request);

        String deviceId = DeviceUtil.generateDeviceId(request);
        String deviceType = client.device != null ? client.device.family : "Unknown";
        String deviceName = client.device != null ? client.device.family : "Unknown";
        String os = client.os != null ? client.userAgent.family : "Unknown";
        String browser = client.userAgent != null ? client.userAgent.family : "Unknown";

        boolean isNewDevice = false;

        UserDevice userDevice = userDeviceRepository
                .findByUserIdAndDeviceId(users.getId(), deviceId)
                .orElseGet(() -> {
                    isNewDevice = true;
                    UserDevice d = new UserDevice();

                    d.setUser(users);
                    d.setDeviceId(deviceId);
                    d.setDeviceName(deviceName);
                    d.setDeviceType(deviceType);
                    d.setOs(os);
                    d.setBrowser(browser);
                    d.setIpAddress(ip);
                    d.setFirstSeenAt(Instant.now());
                    return d;
                });

        userDevice.setLastSeenAt(Instant.now());
        userDevice.setIpAddress(ip);
        userDevice.setActive(true);

        userDeviceRepository.save(userDevice);

        if (isNewDevice) {
            notificationService.sendNewDeviceAlert(users, userDevice);
        }


        return new UserDeviceResponse(
                userDevice.getId(),
                userDevice.getDeviceType(),
                userDevice.getDeviceName(),
                userDevice.getOs(),
                userDevice.getBrowser(),
                userDevice.getLastSeenAt(),
                userDevice.isActive()
        );
    }
}
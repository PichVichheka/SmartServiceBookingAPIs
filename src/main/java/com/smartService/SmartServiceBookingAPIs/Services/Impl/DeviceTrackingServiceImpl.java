package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.Entity.UserDevice;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserDeviceRepository;
import com.smartService.SmartServiceBookingAPIs.Services.DeviceInfoService;
import com.smartService.SmartServiceBookingAPIs.Services.DeviceTrackingService;
import com.smartService.SmartServiceBookingAPIs.Utils.DeviceUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua_parser.Client;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeviceTrackingServiceImpl implements DeviceTrackingService {

    private final UserDeviceRepository userDeviceRepository;
    private final DeviceInfoService deviceInfoService;

    private String extractIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return forwarded != null
                ? forwarded.split(",")[0].trim()
                : request.getRemoteAddr();
    }

    @Override
    public Client trackLogin(Users user, HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");
        String ip = extractIp(request);

        Client client = deviceInfoService.parse(userAgent);
        String deviceId = DeviceUtil.generateDeviceId(request);

        UserDevice userDevice = userDeviceRepository
                .findByDeviceIdAndUserId(deviceId, user.getId())
                .orElseGet(() -> {
                    UserDevice device = new UserDevice();
                    device.setUser(user);
                    device.setDeviceId(deviceId);

                    return device;
                });

        userDevice.setBrowser(
                client.userAgent != null ? client.userAgent.family : "Unknown"
        );
        userDevice.setOs(
                client.os != null ? client.os.family : "Unknown"
        );
        userDevice.setDevice(
                client.device != null ? client.device.family : "Unknown"
        );

        userDevice.setIpAddress(ip);
        userDevice.setLastLogin(LocalDateTime.now());
        userDevice.setActive(true);

        userDeviceRepository.save(userDevice);
        return client;
    }
}

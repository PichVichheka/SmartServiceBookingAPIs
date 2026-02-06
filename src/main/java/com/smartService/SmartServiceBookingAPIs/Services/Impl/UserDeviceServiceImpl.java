package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterDeviceRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.UserDeviceResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.UserDevice;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserDeviceRepository;
import com.smartService.SmartServiceBookingAPIs.Services.UserDeviceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDeviceServiceImpl implements UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;


    @Override
    public UserDeviceResponse registerDevice(
            Users users,
            RegisterDeviceRequest request,
            String ipAddress
    ) {
        Optional<UserDevice> existDevice =
                userDeviceRepository.findByUserIdAndDeviceId(
                        users.getId(), request.getDeviceId()
                );

        UserDevice device;

        if (existDevice.isPresent()) {
            device = existDevice.get();
            device.setLastSeenAt(Instant.now());
            device.setIpAddress(ipAddress);
        } else {
            Instant now = Instant.now();
            device = new UserDevice();
            device.setUser(users);
            device.setDeviceId(request.getDeviceId());
            device.setDeviceType(request.getDeviceType());
            device.setOs(request.getOs());
            device.setBrowser(request.getBrowser());
            device.setIpAddress(ipAddress);
            device.setFirstSeenAt(now);
            device.setLastSeenAt(now);

            userDeviceRepository.save(device);
        }

        return new UserDeviceResponse(
                device.getId(),
                device.getDeviceType(),
                device.getDeviceName(),
                device.getOs(),
                device.getBrowser(),
                device.getLastSeenAt(),
                device.isActive()
        );
    }

    @Override
    public List<UserDeviceResponse> getMyDevices(Users users) {
        return userDeviceRepository.findAllByUserId(users.getId())
                .stream()
                .map(device -> new UserDeviceResponse(
                        device.getId(),
                        device.getDeviceType(),   // system category
                        device.getDeviceName(),   // UI name
                        device.getOs(),
                        device.getBrowser(),
                        device.getLastSeenAt(),
                        device.isActive()
                ))
                .toList();
    }

}

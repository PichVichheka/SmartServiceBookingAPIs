package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterDeviceRequest;
import com.smartService.SmartServiceBookingAPIs.Entity.UserDevice;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserDeviceRepository;
import com.smartService.SmartServiceBookingAPIs.Services.UserDeviceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDeviceServiceImpl implements UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;


    @Override
    public UserDevice registerDevice(
            Users users,
            RegisterDeviceRequest request,
            String ipAddress) {
        Optional<UserDevice> existDevice = userDeviceRepository
                .findByUserIdAndDeviceId(users.getId(), request.getDeviceId());

        if (existDevice.isPresent()) {
            UserDevice device = existDevice.get();
            device.setLastSeenAt(Instant.now());
            device.setIpAddress(ipAddress);

            return device;
        }

        Instant now = Instant.now();
        UserDevice newDevice = new UserDevice();
        newDevice.setUser(users);
        newDevice.setDeviceId(request.getDeviceId());
        newDevice.setDeviceType(request.getDeviceType());
        newDevice.setOs(request.getOs());
        newDevice.setBrowser(request.getBrowser());
        newDevice.setIpAddress(ipAddress);
        newDevice.setFirstSeenAt(now);
        newDevice.setLastSeenAt(now);

        return userDeviceRepository.save(newDevice);
    }
}

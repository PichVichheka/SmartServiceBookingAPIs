package com.smartService.SmartServiceBookingAPIs.Controllers;

import com.smartService.SmartServiceBookingAPIs.DTO.response.UserDeviceResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.UserDevice;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserDeviceController {

    private final UserDeviceRepository userDeviceRepository;

    @GetMapping("/me/device")
    public List<UserDeviceResponse> myDevice(
            @AuthenticationPrincipal Users users
            ) {
        List<UserDevice> devices = userDeviceRepository.findAllByUserId(users.getId());

        return devices.stream()
                .map(device -> new UserDeviceResponse(
                        device.getId(),
                        device.getDeviceType(),
                        device.getOs(),
                        device.getBrowser(),
                        device.getLastSeenAt(),
                        device.isActive()
                ))
                .toList();
    }
}

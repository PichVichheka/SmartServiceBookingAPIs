package com.smartService.SmartServiceBookingAPIs.Controllers;

import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterDeviceRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.UserDeviceResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Services.UserDeviceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserDeviceController {

    private final UserDeviceService userDeviceService;

    @PostMapping("/me/device")
    public UserDeviceResponse registerDevice(
            @AuthenticationPrincipal Users users,
            @RequestBody RegisterDeviceRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipAddress = httpRequest.getRemoteAddr();

        return userDeviceService.registerDevice(
                users,
                request,
                ipAddress
        );
    }

    @GetMapping("/me/device")
    public List<UserDeviceResponse> myDevice(
            @AuthenticationPrincipal Users users
    ) {
        return userDeviceService.getMyDevices(users);
    }
}

package com.smartService.SmartServiceBookingAPIs.Controllers;

import com.smartService.SmartServiceBookingAPIs.DTO.request.AvailabilityRequest;
import com.smartService.SmartServiceBookingAPIs.Services.ProviderAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provider-availability")
@RequiredArgsConstructor
public class ProviderAvailabilityController {
    private final ProviderAvailabilityService availabilityService;

    @PostMapping
    public String createAvailability(
            @RequestBody AvailabilityRequest request) {
        availabilityService.createAvailability(request);
        return "Availability created successfully";
    }
}

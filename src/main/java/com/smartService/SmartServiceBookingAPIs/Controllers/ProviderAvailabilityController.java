package com.smartService.SmartServiceBookingAPIs.Controllers;

import com.smartService.SmartServiceBookingAPIs.DTO.request.AvailabilityRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.AvailabilityResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.Services.ProviderAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/provider/availability")
@RequiredArgsConstructor
public class ProviderAvailabilityController {

    private final ProviderAvailabilityService availabilityService;

    @PostMapping
    public AvailabilityResponse createAvailability(
            @RequestBody AvailabilityRequest request) {
        return availabilityService.createAvailability(request);
    }

    @GetMapping
    public PaginatedResponse<AvailabilityResponse> getAllAvailability(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return availabilityService.getAllAvailability(page, size);
    }

    @GetMapping("/{id}")
    public AvailabilityResponse getAvailabilityById(
            @PathVariable Long id) {
        return availabilityService.getAvailabilityById(id);
    }

    @PutMapping("/{id}")
    public AvailabilityResponse updateAvailability(
            @PathVariable Long id,
            @RequestBody AvailabilityRequest request) {
        return availabilityService.updateAvailability(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteAvailability(
            @PathVariable Long id) {
        availabilityService.deleteAvailability(id);
        return "Availability deleted successfully";
    }
}

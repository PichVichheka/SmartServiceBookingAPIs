package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.AvailabilityRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.AvailabilityResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;

import java.util.List;

public interface ProviderAvailabilityService {
    void createAvailability(AvailabilityRequest request);
    PaginatedResponse<AvailabilityResponse> getAllAvailability(int page, int size);

    AvailabilityResponse getAvailabilityById(Long id);
    AvailabilityResponse updateAvailability(Long id, AvailabilityRequest request);
    AvailabilityResponse updateAvailability(Long id, AvailabilityResponse request);
    void deleteAvailability(Long id);
}

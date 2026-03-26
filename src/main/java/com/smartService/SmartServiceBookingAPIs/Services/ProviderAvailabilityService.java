package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.AvailabilityRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.AvailabilityResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;

public interface ProviderAvailabilityService {
    PaginatedResponse<AvailabilityResponse> getAllAvailability(int page, int size);

    AvailabilityResponse getAvailabilityById(Long id);
    AvailabilityResponse createAvailability(AvailabilityRequest request);
    AvailabilityResponse updateAvailability(Long id, AvailabilityRequest request);
    void deleteAvailability(Long id);
}

package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.ServiceCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.ServiceUpdateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ServiceResponse;

public interface ServiceService {
    PaginatedResponse<ServiceResponse> getAll(int page, int size);

    ServiceResponse getById(Long id);

    ServiceResponse create(ServiceCreateRequest request);

    ServiceResponse update(ServiceUpdateRequest request);

    void delete(Long id);
}

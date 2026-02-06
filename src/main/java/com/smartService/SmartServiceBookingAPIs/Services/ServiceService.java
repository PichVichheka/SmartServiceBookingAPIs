package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.ServiceCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ServiceResponse;

import java.util.Optional;

public interface ServiceService {

    PaginatedResponse<ServiceResponse> getAllServices(int page, int size);

    ServiceResponse getServiceById(Long id);

    ServiceResponse createService(ServiceCreateRequest request);

    ServiceResponse updateService(Long id, ServiceCreateRequest request);

    void deleteService(Long id);

//    Optional<ServiceResponse> getAllByProvider(Long providerId, Long serviceId);
}

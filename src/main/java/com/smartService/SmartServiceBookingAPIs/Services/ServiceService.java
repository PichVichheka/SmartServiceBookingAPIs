package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.ServiceCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ServiceResponse;

import java.util.List;

public interface ServiceService {

    ServiceResponse createService(ServiceCreateRequest request);

    ServiceResponse updateService(Long id, ServiceCreateRequest request);

    void deleteService(Long id);

    ServiceResponse getServiceById(Long id);

    List<ServiceResponse> getAllServices();
}

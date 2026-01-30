package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.ServiceCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ServiceResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Services;
//import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.ServicesRepository;
//import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import com.smartService.SmartServiceBookingAPIs.Services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServicesRepository servicesRepository;
//    private final UserRepository userRepository;

    @Override
    public ServiceResponse createService(ServiceCreateRequest request) {
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Price must be greater than 0");
        }
        if (request.getServiceType() == null || request.getServiceType().isBlank()) {
            throw new RuntimeException("Service type cannot be empty");
        }

        Services service = Services.builder()
                .serviceType(request.getServiceType())
                .serviceDescription(request.getServiceDescription())
                .serviceOffer(request.getServiceOffer())
                .price(request.getPrice())
                .priceUnit(request.getPriceUnit())
                .build();

        Services saved = servicesRepository.save(service);
        return mapToResponse(saved);
    }

    @Override
    public ServiceResponse updateService(Long id, ServiceCreateRequest request) {
        return null;
    }

    @Override
    public void deleteService(Long id) {
    }

    @Override
    public ServiceResponse getServiceById(Long id) {
        return null;
    }

    @Override
    public List<ServiceResponse> getAllServices() {
        return null;
    }

    private ServiceResponse mapToResponse(Services service) {
        return ServiceResponse.builder()
                .id(service.getId())
                .serviceType(service.getServiceType())
                .serviceDescription(service.getServiceDescription())
                .serviceOffer(service.getServiceOffer())
                .price(service.getPrice())
                .priceUnit(service.getPriceUnit())
                .providerId(service.getProvider() != null ? service.getProvider().getId() : null)
                .providerName(service.getProvider() != null ? service.getProvider().getName() : null)
                .build();
    }
}

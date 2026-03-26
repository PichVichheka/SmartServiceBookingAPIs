package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.ServiceCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ServiceResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Services;
import com.smartService.SmartServiceBookingAPIs.Repositories.ServicesRepository;
import com.smartService.SmartServiceBookingAPIs.Services.Jwt.JwtService;
import com.smartService.SmartServiceBookingAPIs.Services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.notFound;
import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.validation;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServicesRepository servicesRepository;
    private final JwtService jwtService;

    @Override
    public PaginatedResponse<ServiceResponse> getAllServices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Services> servicesPage = servicesRepository.findAll(pageable);

        List<ServiceResponse> serviceResponses = servicesPage.getContent()
                .stream()
                .map(services -> {
                    ServiceResponse response = new ServiceResponse();
                    response.setId(services.getId());
                    response.setServiceType(services.getServiceType());
                    response.setServiceDescription(services.getServiceDescription());
                    response.setServiceOffer(services.getServiceOffer());
                    response.setPrice(services.getPrice());
                    response.setPriceUnit(services.getPriceUnit());

                    return response;
                }).toList();
        PaginatedResponse.PaginationMeta paginationMeta =
                new PaginatedResponse.PaginationMeta(
                        servicesPage.getNumber() + 1,
                        servicesPage.getSize(),
                        servicesPage.getTotalElements(),
                        servicesPage.getTotalPages(),
                        servicesPage.hasNext(),
                        servicesPage.hasPrevious()
                );

        return new PaginatedResponse<>(serviceResponses, paginationMeta);
    }

    @Override
    public ServiceResponse getServiceById(Long id) {
        Services servicesGetId = servicesRepository.findById(id)
                .orElseThrow(() -> notFound("Service not found with id:" + id));


        return mapToResponse(servicesGetId);
    }

    @Override
    public ServiceResponse createService(ServiceCreateRequest request) {

        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw validation("Price must be greater than 0");
        }
        if (request.getServiceType() == null || request.getServiceType().isBlank()) {
            throw validation("Service type cannot be empty");
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
        Services servicesUpdate = servicesRepository.findById(id)
                .orElseThrow(() -> notFound("Services not found with id:" + id));

        servicesUpdate.setServiceDescription(request.getServiceDescription());
        servicesUpdate.setServiceOffer(request.getServiceOffer());

        return mapToResponse(servicesRepository.save(servicesUpdate));
    }

    @Override
    public void deleteService(Long id) {
        Services servicesDelete = servicesRepository.findById(id)
                .orElseThrow(() -> notFound("Service not found with id:" + id));

        servicesRepository.delete(servicesDelete);

    }

//    @Override
//    public ServiceResponse getByProviderAndServiceId(Long providerId, Long serviceId) {
//
//        Optional<ServiceResponse> service = servicesRepository
//                .findByProviderId(providerId, serviceId);
//
//        return mapToResponse(service);
//    }


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

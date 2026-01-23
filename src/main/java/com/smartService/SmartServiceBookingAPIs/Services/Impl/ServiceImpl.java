package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.ServiceCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.ServiceUpdateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ServiceResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Services;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.ServiceRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import com.smartService.SmartServiceBookingAPIs.Services.Jwt.JwtService;
import com.smartService.SmartServiceBookingAPIs.Services.ServiceService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.forbidden;

@Service
@RequiredArgsConstructor
public class ServiceImpl implements ServiceService {

    private final JwtService jwtService;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    @Override
    public PaginatedResponse<ServiceResponse> getAll(int page, int size) {
        return null;
    }

    @Override
    public ServiceResponse getById(Long id) {
        return null;
    }

    @Override
    public ServiceResponse create(ServiceCreateRequest request) {

        Users currentUser = jwtService.getCurrentUser();

        if (!currentUser.hasRole("provider")) {
            throw forbidden("Only providers can create services");
        }

        Services service = Services.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .provider(currentUser)
                .build();

        serviceRepository.save(service);

        return mapToResponse(service);
    }

    @Override
    public ServiceResponse update(ServiceUpdateRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}

package com.smartService.SmartServiceBookingAPIs.Services.Impl;


import com.smartService.SmartServiceBookingAPIs.DTO.request.AvailabilityRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.AvailabilityResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.ProviderAvailability;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.ProviderAvailabilityRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import com.smartService.SmartServiceBookingAPIs.Services.ProviderAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.notFound;

@Service
@RequiredArgsConstructor
public class ProviderAvailabilityServiceImpl implements ProviderAvailabilityService {

    private final ProviderAvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;


    @Override
    public PaginatedResponse<AvailabilityResponse> getAllAvailability(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProviderAvailability> availabilityPage = availabilityRepository.findAll(pageable);

        List<AvailabilityResponse> data = availabilityPage.getContent()
                .stream()
                .map(providerAvailability -> {
                    AvailabilityResponse responseData = new AvailabilityResponse();
                    responseData.setId(providerAvailability.getId());
                    responseData.setProviderId(providerAvailability.getProvider().getId());
                    responseData.setDate(providerAvailability.getAvailableDate());
                    responseData.setStartTime(providerAvailability.getStartTime());
                    responseData.setEndTime(providerAvailability.getEndTime());
                    responseData.setAvailable(providerAvailability.isAvailable());

                    return responseData;
                })
                .toList();

        PaginatedResponse.PaginationMeta paginationMeta =
                new PaginatedResponse.PaginationMeta(
                        availabilityPage.getNumber() + 1,
                        availabilityPage.getSize(),
                        availabilityPage.getTotalElements(),
                        availabilityPage.getTotalPages(),
                        availabilityPage.hasNext(),
                        availabilityPage.hasPrevious()
                );

        return new PaginatedResponse<>(data, paginationMeta);
    }

    @Override
    public AvailabilityResponse getAvailabilityById(Long id) {
        ProviderAvailability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> notFound("Availability not found."));

        AvailabilityResponse response = new AvailabilityResponse();
        response.setId(availability.getId());
        response.setProviderId(availability.getProvider().getId());
        response.setDate(availability.getAvailableDate());
        response.setStartTime(availability.getStartTime());
        response.setEndTime(availability.getEndTime());
        response.setAvailable(availability.isAvailable());

        return response;
    }

    @Override
    public AvailabilityResponse createAvailability(AvailabilityRequest request) {
        Users provider = userRepository.findById(request.getProviderId())
                .orElseThrow(() -> notFound("provider not found"));

        ProviderAvailability availability = new ProviderAvailability();
        availability.setProvider(provider);
        availability.setAvailableDate(request.getDate());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setAvailable(true);

        availabilityRepository.save(availability);

        AvailabilityResponse response = new AvailabilityResponse();
        response.setId(availability.getId());
        response.setProviderId(provider.getId());
        response.setDate(availability.getAvailableDate());
        response.setStartTime(availability.getStartTime());
        response.setEndTime(availability.getEndTime());
        response.setAvailable(availability.isAvailable());

        return response;
    }

    @Override
    public AvailabilityResponse updateAvailability(Long id, AvailabilityRequest request) {
        ProviderAvailability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> notFound("Availability not found."));

        if (request.getProviderId() != null) {
            Users provider = userRepository.findById(request.getProviderId())
                    .orElseThrow(() -> notFound("provider not found"));
            availability.setProvider(provider);
        }

        availability.setAvailableDate(request.getDate());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());

        availabilityRepository.save(availability);

        AvailabilityResponse response = new AvailabilityResponse();
        response.setId(availability.getId());
        response.setProviderId(availability.getProvider().getId());
        response.setDate(availability.getAvailableDate());
        response.setStartTime(availability.getStartTime());
        response.setEndTime(availability.getEndTime());
        response.setAvailable(availability.isAvailable());

        return response;
    }

    @Override
    public void deleteAvailability(Long id) {
        ProviderAvailability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> notFound("Availability not found."));

        availabilityRepository.delete(availability);
    }
}
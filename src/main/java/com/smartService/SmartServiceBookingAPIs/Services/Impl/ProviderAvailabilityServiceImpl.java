package com.smartService.SmartServiceBookingAPIs.Services.Impl;


import com.smartService.SmartServiceBookingAPIs.DTO.request.AvailabilityRequest;
import com.smartService.SmartServiceBookingAPIs.Entity.ProviderAvailability;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.ProviderAvailabilityRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import com.smartService.SmartServiceBookingAPIs.Services.ProviderAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.notFound;

@Service
@RequiredArgsConstructor
public class ProviderAvailabilityServiceImpl implements ProviderAvailabilityService {

    private final ProviderAvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;

    @Override
    public void createAvailability(AvailabilityRequest request) {
        Users provider = userRepository.findById(request.getProviderId())
                .orElseThrow(() -> notFound("provider not found"));

        ProviderAvailability availability = new ProviderAvailability();
        availability.setProvider(provider);
        availability.setAvailableDate(request.getDate());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setAvailable(true);

        availabilityRepository.save(availability);


    }
}

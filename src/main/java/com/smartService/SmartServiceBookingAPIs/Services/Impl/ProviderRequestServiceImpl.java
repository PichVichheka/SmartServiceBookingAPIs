package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.BecomeProviderRequest;
import com.smartService.SmartServiceBookingAPIs.Entity.ProviderRequest;
import com.smartService.SmartServiceBookingAPIs.Entity.RequestStatus;
import com.smartService.SmartServiceBookingAPIs.Entity.Roles;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.ProviderRequestRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import com.smartService.SmartServiceBookingAPIs.Services.ProviderRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.*;

@Service
@RequiredArgsConstructor
public class ProviderRequestServiceImpl implements ProviderRequestService {

    private final UserRepository userRepository;
    private final ProviderRequestRepository providerRequestRepository;

    @Override
    public ProviderRequest requestProvider(Long userId, BecomeProviderRequest requestDto) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if already has pending request
        if (providerRequestRepository.existsByUserIdAndStatus(userId, RequestStatus.pending)) {
            throw new RuntimeException("You already have a pending request");
        }

        ProviderRequest providerRequest = new ProviderRequest();
        providerRequest.setUser(user);
        providerRequest.setServiceType(requestDto.getServiceType());
        providerRequest.setServiceDescription(requestDto.getServiceDescription());
        providerRequest.setExperience(requestDto.getExperience());
        providerRequest.setStatus(RequestStatus.pending);

        return providerRequestRepository.save(providerRequest);
    }

    @Override
    public List<ProviderRequest> getPendingRequests() {
        return providerRequestRepository.findByStatus(RequestStatus.pending);
    }

    @Override
    @Transactional
    public ProviderRequest handleRequest(Long requestId, boolean accept) {
        ProviderRequest request = providerRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (accept) {
            request.setStatus(RequestStatus.accepted);

            Users user = request.getUser();
            boolean hasProviderRole = user.getRoles().stream()
                    .anyMatch(r -> r.getName().equalsIgnoreCase("provider"));

            if (!hasProviderRole) {
                Roles providerRole = new Roles();
                providerRole.setName("provider");
                user.getRoles().add(providerRole);
                userRepository.save(user);
            }
        } else {
            request.setStatus(RequestStatus.ignore);
        }

        return providerRequestRepository.save(request);
    }
}
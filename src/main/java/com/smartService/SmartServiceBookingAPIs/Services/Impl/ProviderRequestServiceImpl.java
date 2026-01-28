package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.BecomeProviderRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ProviderRequestResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.UserSummaryResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.ProviderRequest;
import com.smartService.SmartServiceBookingAPIs.Entity.RequestStatus;
import com.smartService.SmartServiceBookingAPIs.Entity.Roles;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.ProviderRequestRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.RoleRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import com.smartService.SmartServiceBookingAPIs.Services.ProviderRequestService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.*;

@Service
@RequiredArgsConstructor
public class ProviderRequestServiceImpl implements ProviderRequestService {

    private final UserRepository userRepository;
    private final ProviderRequestRepository providerRequestRepository;
    private final RoleRepository roleRepository;

    @Override
    public ProviderRequestResponse requestProvider(Long userId, BecomeProviderRequest requestDto) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> notFound("User not found"));

        // Already provider → no request allowed
        boolean isAlreadyProvider = user.getRoles().stream()
                .anyMatch(r -> r.getName().equalsIgnoreCase("provider"));


        if (isAlreadyProvider) {
            throw badRequest("You are already a provider");
        }

        // Check if already has pending request
        if (providerRequestRepository.existsByUserIdAndStatus(userId, RequestStatus.pending)) {
            throw badRequest("You already have a pending request");
        }

        ProviderRequest providerRequest = new ProviderRequest();
        providerRequest.setUser(user);
        providerRequest.setServiceType(requestDto.getServiceType());
        providerRequest.setServiceDescription(requestDto.getServiceDescription());
        providerRequest.setExperience(requestDto.getExperience());
        providerRequest.setStatus(RequestStatus.pending);

        ProviderRequest savedProviderRequest = providerRequestRepository.save(providerRequest);

        return mapToResponse(savedProviderRequest);
    }

    @Override
    public List<ProviderRequestResponse> getPendingRequests() {
        return providerRequestRepository.findByStatus(RequestStatus.pending)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProviderRequestResponse handleRequest(Long requestId, boolean accept) {
        ProviderRequest request = providerRequestRepository.findById(requestId)
                .orElseThrow(() -> notFound("Request not found"));

        if (accept) {
            request.setStatus(RequestStatus.accepted);

            Users user = request.getUser();
            boolean hasProviderRole = user.getRoles().stream()
                    .anyMatch(r -> r.getName().equalsIgnoreCase("provider"));

            if (!hasProviderRole) {
                Roles providerRole = roleRepository.findByName("provider")
                        .orElseThrow(() -> notFound("Role not found."));
                user.getRoles().add(providerRole);
                userRepository.save(user);
            }
        } else {
            request.setStatus(RequestStatus.ignore);
        }

        ProviderRequest saved = providerRequestRepository.save(request);

        return mapToResponse(saved);
    }

    private ProviderRequestResponse mapToResponse(ProviderRequest request) {
        UserSummaryResponse userDto = new UserSummaryResponse();
        userDto.setId(request.getUser().getId());
        userDto.setFullname(request.getUser().getFullname());
        userDto.setEmail(request.getUser().getEmail());
        userDto.setPhone(request.getUser().getPhone());

        ProviderRequestResponse response = new ProviderRequestResponse();
        response.setId(request.getId());
        response.setServiceType(request.getServiceType());
        response.setServiceDescription(request.getServiceDescription());
        response.setExperience(request.getExperience());
        response.setStatus(request.getStatus());
        response.setUser(userDto);

        return response;
    }

}
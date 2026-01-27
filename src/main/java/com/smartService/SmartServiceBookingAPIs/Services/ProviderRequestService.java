package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.BecomeProviderRequest;
import com.smartService.SmartServiceBookingAPIs.Entity.ProviderRequest;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import java.util.List;

public interface ProviderRequestService {
    ProviderRequest requestProvider(Long userId, BecomeProviderRequest request);
    List<ProviderRequest> getPendingRequests();
    ProviderRequest handleRequest(Long requestId, boolean accept);  // Changed from userId to requestId
}
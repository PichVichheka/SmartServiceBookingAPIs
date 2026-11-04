package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.BecomeProviderRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ProviderRequestResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.ProviderRequest;
import java.util.List;

public interface ProviderRequestService {
    ProviderRequestResponse requestProvider(Long userId, BecomeProviderRequest request);
    List<ProviderRequestResponse> getPendingRequests();
    ProviderRequestResponse handleRequest(Long requestId, boolean accept);  // Changed from userId to requestId
}
package com.smartService.SmartServiceBookingAPIs.Controllers;

import com.smartService.SmartServiceBookingAPIs.DTO.request.BecomeProviderRequest;
import com.smartService.SmartServiceBookingAPIs.Entity.ProviderRequest;
import com.smartService.SmartServiceBookingAPIs.Services.ProviderRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class ProviderRequestController {

    private final ProviderRequestService providerRequestService;

    @PostMapping("/request/become-a-provider")
    public ResponseEntity<ProviderRequest> requestProvider(
            @RequestParam Long userId,
            @RequestBody BecomeProviderRequest request
    ) {
        return ResponseEntity.ok(providerRequestService.requestProvider(userId, request));
    }

    @GetMapping("/get/provider-request")
    public ResponseEntity<List<ProviderRequest>> getPendingRequests() {
        return ResponseEntity.ok(providerRequestService.getPendingRequests());
    }

    @PutMapping("/provider-request/{requestId}")  // Changed to requestId
    public ResponseEntity<ProviderRequest> handleRequest(
            @PathVariable Long requestId,
            @RequestParam boolean accept
    ) {
        return ResponseEntity.ok(providerRequestService.handleRequest(requestId, accept));
    }
}

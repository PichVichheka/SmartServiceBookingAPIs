package com.smartService.SmartServiceBookingAPIs.Controllers;

import com.smartService.SmartServiceBookingAPIs.DTO.request.BecomeProviderRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ApiResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ProviderRequestResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.ProviderRequest;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Services.Jwt.JwtService;
import com.smartService.SmartServiceBookingAPIs.Services.ProviderRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProviderRequestController {

    private final ProviderRequestService providerRequestService;
    private final JwtService jwtService;

    @PostMapping("/customer/become-a-provider")
    public ResponseEntity<ApiResponse<ProviderRequestResponse>> requestProvider(
            @RequestBody BecomeProviderRequest request
    ) {
        Users currentUser = jwtService.getCurrentUser();
        ProviderRequestResponse providerRequestResponse = providerRequestService.requestProvider(currentUser.getId(), request);
        ApiResponse<ProviderRequestResponse> response = new ApiResponse<>(
                true,
                "Provider request submitted successfully",
                providerRequestResponse
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/admin/provider-request")
    public ResponseEntity<ApiResponse<List<ProviderRequestResponse>>> getPendingRequests() {
        List<ProviderRequestResponse> requests = providerRequestService.getPendingRequests();
        ApiResponse<List<ProviderRequestResponse>> response = new ApiResponse<>(
                true,
                "Pending requests retrieved successfully",
                requests
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/provider-request/{requestId}")
    public ResponseEntity<ApiResponse<ProviderRequestResponse>> handleRequest(
            @PathVariable Long requestId,
            @RequestParam boolean accept
    ) {
        ProviderRequestResponse ProviderRequestResponse = providerRequestService.handleRequest(requestId, accept);
        String message = accept ?
                "Provider request approved successfully" :
                "Provider request rejected successfully";
        ApiResponse<ProviderRequestResponse> response = new ApiResponse<>(
                true,
                message,
                ProviderRequestResponse
        );
        return ResponseEntity.ok(response);
    }
}
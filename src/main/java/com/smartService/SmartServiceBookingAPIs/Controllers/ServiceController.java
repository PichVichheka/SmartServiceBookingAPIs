package com.smartService.SmartServiceBookingAPIs.Controllers;

import com.smartService.SmartServiceBookingAPIs.DTO.request.ServiceCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ApiResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ServiceResponse;
import com.smartService.SmartServiceBookingAPIs.Services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;


    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<ServiceResponse>>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int size
    ) {
        PaginatedResponse<ServiceResponse> servicesRes = serviceService.getAllServices(page, size);
        ApiResponse<PaginatedResponse<ServiceResponse>> response  = new ApiResponse<>(
                true,
                "Get all Service successfully.",
                servicesRes
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getService(@PathVariable Long id) {
        ServiceResponse servicesResId = serviceService.getServiceById(id);
        ApiResponse<ServiceResponse> responseId = new ApiResponse<>(
                true,
                "Get Service successfully.",
                servicesResId
        );

        return ResponseEntity.ok(responseId);
    }

    @PostMapping
    public ResponseEntity<ServiceResponse> createService(
            @RequestBody ServiceCreateRequest request
    ) {
        ServiceResponse createRes = serviceService.createService(request);
        ApiResponse<ServiceResponse> postResponse = new ApiResponse<>(
                true,
                "Created Service successfully.",
                createRes
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse.getData());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(
            @PathVariable Long id,
            @RequestBody ServiceCreateRequest request
    ) {
        ServiceResponse updateRes = serviceService.updateService(id, request);
        ApiResponse<ServiceResponse> responseUpdate = new ApiResponse<>(
                true,
                "Updated service successfully.",
                updateRes
        );

        return ResponseEntity.ok(responseUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {

        serviceService.deleteService(id);
        ApiResponse<Void> responseDelete = new ApiResponse<>(
                true,
                "Delete service successfully."
        );

        return ResponseEntity.noContent().build();
    }
}

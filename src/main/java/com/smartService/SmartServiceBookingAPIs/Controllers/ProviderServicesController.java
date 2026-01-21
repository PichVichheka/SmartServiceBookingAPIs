package com.smartService.SmartServiceBookingAPIs.Controllers;

import com.smartService.SmartServiceBookingAPIs.Entity.Services;
import com.smartService.SmartServiceBookingAPIs.Services.ServiceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provider/services")
public class ProviderServicesController {

    private final ServiceService serviceService;

    public ProviderServicesController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    // ✅ CREATE SERVICE
    @PreAuthorize("hasRole('provider')")
    @PostMapping
    public Services createService(@RequestBody Services services) {
        return serviceService.createService(services);
    }

    // ✅ GET ALL SERVICES (only for provider)
    @PreAuthorize("hasRole('provider')")
    @GetMapping
    public List<Services> getMyServices(@RequestParam Integer providerId) {
        return serviceService.getServicesByProviderId(providerId);
    }

    // ✅ GET SERVICE BY ID
    @PreAuthorize("hasRole('provider')")
    @GetMapping("/{id}")
    public Services getServiceById(@PathVariable Integer id) {
        return serviceService.getServiceById(id);
    }

    // ✅ UPDATE SERVICE
    @PreAuthorize("hasRole('provider')")
    @PutMapping("/{id}")
    public Services updateService(
            @PathVariable Integer id,
            @RequestParam Integer providerId,
            @RequestBody Services request
    ) {
        // Check ownership inside service layer or here
        Services service = serviceService.getServiceById(id);
        if (!service.getProvider().getId().equals(providerId)) {
            throw new RuntimeException("You are not allowed to update this service");
        }
        return serviceService.updateService(id, request);
    }

    // ✅ DELETE SERVICE
    @PreAuthorize("hasRole('provider')")
    @DeleteMapping("/{id}")
    public void deleteService(
            @PathVariable Integer id,
            @RequestParam Integer providerId
    ) {
        Services service = serviceService.getServiceById(id);
        if (!service.getProvider().getId().equals(providerId)) {
            throw new RuntimeException("You are not allowed to delete this service");
        }
        serviceService.deleteService(id);
    }
}

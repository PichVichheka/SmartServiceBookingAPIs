package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.Entity.Services;
import com.smartService.SmartServiceBookingAPIs.Repositories.ServiceRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import com.smartService.SmartServiceBookingAPIs.Services.ServiceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private ServiceServiceImpl serviceService;

    public ServiceServiceImpl(ServiceRepository serviceRepository, UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Services createService(Services services) {
        return serviceRepository.save(services);
    }

    @Override
    public Services getServiceById(Integer id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('admin')")
    public List<Services> getAllServices() {
        return serviceService.getAllServices();
    }


    @Override
    public List<Services> getServicesByProviderId(Integer providerId) {
        return serviceRepository.findByProvider(
                userRepository.findById(Long.valueOf(providerId))
                        .orElseThrow(() -> new EntityNotFoundException("Provider not found"))
        );
    }

    @Override
    public Services updateService(Integer id, Services services) {
        Services existingService = getServiceById(id);

        existingService.setServiceType(services.getServiceType());
        existingService.setServiceDescription(services.getServiceDescription());
        existingService.setServiceOffer(services.getServiceOffer());
        existingService.setPrice(services.getPrice());
        existingService.setPriceUnit(services.getPriceUnit());

        return serviceRepository.save(existingService);
    }

    @Override
    public void deleteService(Integer id) {
        Services service = getServiceById(id);
        serviceRepository.delete(service);
    }
}

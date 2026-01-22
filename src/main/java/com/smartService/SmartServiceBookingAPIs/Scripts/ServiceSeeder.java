package com.smartService.SmartServiceBookingAPIs.Scripts;

import com.smartService.SmartServiceBookingAPIs.Entity.Services;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.ServiceRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.notFound;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(3)
public class ServiceSeeder implements CommandLineRunner {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding...");

        if (serviceRepository.count() == 0) {
            seedService();
            log.info("Seeding completed");
        } else {
            log.info("Service already exist. Skipping seeding.");
        }
    }

    private void seedService() {
        List<Users> providers = userRepository.findAll();

        if (providers.isEmpty()) {
            throw notFound("No provider found.");
        }

        Users provider1 = providers.getFirst();

        List<Services> services = Arrays.asList(
                createService(
                        "Plumbing",
                        "Professional plumbing services including pipe repairs, leak detection, and installation",
                        "10% off for first-time customers",
                        new BigDecimal("50.00"),
                        provider1
                ),
                createService(
                        "Electrical Work",
                        "Licensed electrician for wiring, repairs, and electrical installations",
                        "Free safety inspection with any service",
                        new BigDecimal("75.00"),
                        provider1
                )
        );

        serviceRepository.saveAll(services);
        log.info("Seeded {} completed.", services.size());
    }

    private Services createService(String type, String description, String offer,
                                   BigDecimal price, Users provider) {
        Services service = new Services();
        service.setServiceType(type);
        service.setServiceDescription(description);
        service.setServiceOffer(offer);
        service.setPrice(price);
        service.setPriceUnit("per hour");
        service.setProvider(provider);
        return service;
    }

}

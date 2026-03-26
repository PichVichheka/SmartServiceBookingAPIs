package com.smartService.SmartServiceBookingAPIs.Repositories;

//import com.smartService.SmartServiceBookingAPIs.DTO.response.ServiceResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.util.Optional;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {
    // Optional: Add custom queries here
//    Optional<ServiceResponse> findByProviderId(Long providerId, Long serviceId);
}

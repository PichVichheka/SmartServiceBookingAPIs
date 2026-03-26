package com.smartService.SmartServiceBookingAPIs.Repositories;

//import com.smartService.SmartServiceBookingAPIs.DTO.response.ProviderRequestResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.ProviderRequest;
import com.smartService.SmartServiceBookingAPIs.Entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProviderRequestRepository extends JpaRepository<ProviderRequest, Long> {
    List<ProviderRequest> findByStatus(RequestStatus status);
//    List<ProviderRequestResponse> findByStatus(RequestStatus status);
    boolean existsByUserIdAndStatus(Long userId, RequestStatus status);
}
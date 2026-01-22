package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.Entity.Services;
import java.util.List;

public interface ServiceService {

    Services createService(Services services);

    Services getServiceById(Integer id);

    List<Services> getAllServices();  // ← PUT IT HERE

    List<Services> getServicesByProviderId(Integer providerId);

    Services updateService(Integer id, Services services);

    void deleteService(Integer id);
}

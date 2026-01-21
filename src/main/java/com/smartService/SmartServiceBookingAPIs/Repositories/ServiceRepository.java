package com.smartService.SmartServiceBookingAPIs.Repositories;

import com.smartService.SmartServiceBookingAPIs.Entity.Services;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Services, Integer> {

    List<Services> findByProvider(Users provider);

}

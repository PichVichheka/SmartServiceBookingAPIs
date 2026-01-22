package com.smartService.SmartServiceBookingAPIs.Repositories;

import com.smartService.SmartServiceBookingAPIs.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}

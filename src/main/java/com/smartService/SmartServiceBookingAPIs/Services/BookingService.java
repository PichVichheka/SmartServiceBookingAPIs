package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.Entity.Booking;

public interface BookingService {
    Booking createBooking(Long serviceId, Long availabilityId);
    Booking confirmBooking(Long bookingId);
    void cancelBooking(Long bookingId);
}

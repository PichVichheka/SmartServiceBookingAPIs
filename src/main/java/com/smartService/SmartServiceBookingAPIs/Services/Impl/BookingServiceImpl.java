package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.Entity.Booking;
import com.smartService.SmartServiceBookingAPIs.Entity.ProviderAvailability;
import com.smartService.SmartServiceBookingAPIs.Entity.Services;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.BookingRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.ProviderAvailabilityRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.ServiceRepository;
import com.smartService.SmartServiceBookingAPIs.Services.BookingService;
import com.smartService.SmartServiceBookingAPIs.Services.Jwt.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.notFound;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final ServiceRepository serviceRepository;
    private final ProviderAvailabilityRepository providerAvailabilityRepository;
    private final BookingRepository bookingRepository;
    private final JwtService jwtService;

    @Transactional
    @Override
    public Booking createBooking(Long serviceId, Long availabilityId) {
        Users user = jwtService.getCurrentUser();

        Services services = serviceRepository.findById(serviceId).orElseThrow(() -> notFound("Service not found"));
        ProviderAvailability slot = providerAvailabilityRepository.findById(availabilityId).orElseThrow(() -> notFound("Availability not found"));

        if (!slot.isAvailable()) {
            throw new RuntimeException("Time slot already booked");
        }

        slot.setAvailable(false);
        providerAvailabilityRepository.save(slot);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setService(services);
        booking.setAvailability(slot);
        booking.setBookingDate(LocalDate.now());
        booking.setStatus("Pending");
        booking.setTotalPrice(services.getPrice());

        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setStatus("Confirmed");

        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setStatus("Cancelled");

        ProviderAvailability slot = booking.getAvailability();
        slot.setAvailable(true);

        providerAvailabilityRepository.save(slot);
        bookingRepository.save(booking);
    }
}

package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.BookingReqest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.BookingResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Booking;
import com.smartService.SmartServiceBookingAPIs.Entity.ProviderAvailability;
import com.smartService.SmartServiceBookingAPIs.Entity.Services;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.BookingRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.ProviderAvailabilityRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.ServicesRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import com.smartService.SmartServiceBookingAPIs.Services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.notFound;

@RequiredArgsConstructor
@Service

public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ServicesRepository servicesRepository;
    private final ProviderAvailabilityRepository providerAvailabilityRepository;


    @Override
    public BookingResponse createBooking(BookingReqest reqest) {

        Users users = userRepository.findById(reqest.getUserId())
                .orElseThrow(() -> notFound("user not found"));

        Users provider = userRepository.findById(reqest.getProviderId())
                .orElseThrow(() -> notFound("provider not found"));

        Services service = servicesRepository.findById(reqest.getServiceId())
                .orElseThrow(() -> notFound("service not found"));

        ProviderAvailability availability = providerAvailabilityRepository.findById(reqest.getAvailabilityId())
                .orElseThrow(() -> notFound("availability not found"));


        Booking booking = new Booking();
        booking.setUser(users);
        booking.setProvider(provider);
        booking.setService(service);
        booking.setAvailability(availability);
        booking.setBookingDate(reqest.getBookingDate());
        booking.setTotalPrice(service.getPrice()); // price from service
        booking.setStatus("PENDING");


        Booking savedBooking = bookingRepository.save(booking);
        return BookingResponse.builder()
                .id(savedBooking.getId())
                .userId(users.getId())
                .providerId(provider.getId())
                .serviceId(service.getId())
                .availabilityId(availability.getId())
                .bookingDate(savedBooking.getBookingDate())
                .totalPrice(savedBooking.getTotalPrice())
                .status(savedBooking.getStatus())
                .build();
    }

    @Override
    public List<BookingResponse> getallBooking() {
        return bookingRepository.findAll().stream().map(booking ->
                BookingResponse.builder()
                        .id(booking.getId())
                        .userId(booking.getUser().getId())
                        .providerId(booking.getProvider().getId())
                        .serviceId(booking.getService().getId())
                        .availabilityId(booking.getAvailability().getId())
                        .bookingDate(booking.getBookingDate())
                        .totalPrice(booking.getTotalPrice())
                        .status(booking.getStatus())
                        .build()
        ).toList();

    }

    @Override
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> notFound("booking not found"));

        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .providerId(booking.getProvider().getId())
                .serviceId(booking.getService().getId())
                .availabilityId(booking.getAvailability().getId())
                .bookingDate(booking.getBookingDate())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .build();
    }
}

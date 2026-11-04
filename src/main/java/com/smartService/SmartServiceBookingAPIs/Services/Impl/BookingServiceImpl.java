package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.BookingRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.BookingResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public BookingResponse createBooking(BookingRequest request) {

        Users users = userRepository.findById(request.getUserId())
                .orElseThrow(() -> notFound("user not found"));

        Users provider = userRepository.findById(request.getProviderId())
                .orElseThrow(() -> notFound("provider not found"));

        Services service = servicesRepository.findById(request.getServiceId())
                .orElseThrow(() -> notFound("service not found"));

        ProviderAvailability availability = providerAvailabilityRepository.findById(request.getAvailabilityId())
                .orElseThrow(() -> notFound("availability not found"));


        Booking booking = new Booking();
        booking.setUser(users);
        booking.setProvider(provider);
        booking.setService(service);
        booking.setAvailability(availability);
        booking.setBookingDate(request.getBookingDate());
        booking.setTotalPrice(service.getPrice()); // price from service
        booking.setStatus("PENDING");


        Booking savedBooking = bookingRepository.save(booking);

        BookingResponse response = new BookingResponse();
        response.setId(savedBooking.getId());
        response.setUserId(users.getId());
        response.setProviderId(provider.getId());
        response.setServiceId(service.getId());
        response.setAvailabilityId(availability.getId());
        response.setBookingDate(savedBooking.getBookingDate());
        response.setTotalPrice(savedBooking.getTotalPrice());
        response.setStatus(savedBooking.getStatus());

        return response;
    }

    @Override
    public PaginatedResponse<BookingResponse> getAllBooking(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> bookingPage = bookingRepository.findAll(pageable);

        List<BookingResponse> bookingResponses = bookingPage.getContent()
                .stream()
                .map(booking -> {
                    BookingResponse response = new BookingResponse();
                    response.setId(booking.getId());
                    response.setUserId(booking.getUser().getId());
                    response.setProviderId(booking.getProvider().getId());
                    response.setServiceId(booking.getService().getId());
                    response.setAvailabilityId(booking.getAvailability().getId());
                    response.setBookingDate(booking.getBookingDate());
                    response.setTotalPrice(booking.getTotalPrice());
                    response.setStatus(booking.getStatus());
                    return response;
                }).toList();

        PaginatedResponse.PaginationMeta paginationMeta =
                new PaginatedResponse.PaginationMeta(
                        bookingPage.getNumber() + 1,
                        bookingPage.getSize(),
                        bookingPage.getTotalElements(),
                        bookingPage.getTotalPages(),
                        bookingPage.hasNext(),
                        bookingPage.hasPrevious()
                );

        return new PaginatedResponse<>(bookingResponses, paginationMeta);
    }


    @Override
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> notFound("booking not found"));

        BookingResponse bookingIdResponse = new BookingResponse();
        bookingIdResponse.setId(booking.getId());
        bookingIdResponse.setUserId(booking.getUser().getId());
        bookingIdResponse.setProviderId(booking.getProvider().getId());
        bookingIdResponse.setServiceId(booking.getService().getId());
        bookingIdResponse.setAvailabilityId(booking.getAvailability().getId());
        bookingIdResponse.setBookingDate(booking.getBookingDate());
        bookingIdResponse.setTotalPrice(booking.getTotalPrice());
        bookingIdResponse.setStatus(booking.getStatus());

        return bookingIdResponse;
    }

}

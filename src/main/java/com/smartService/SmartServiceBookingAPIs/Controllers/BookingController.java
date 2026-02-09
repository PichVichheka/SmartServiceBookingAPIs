package com.smartService.SmartServiceBookingAPIs.Controllers;

import com.smartService.SmartServiceBookingAPIs.DTO.request.BookingReqest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ApiResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.BookingResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.Services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/customer/bookings")
    public ApiResponse<BookingResponse> createBooking(@RequestBody BookingReqest request) {
        BookingResponse booking = bookingService.createBooking(request);
        return new ApiResponse<>(true, "Booking created successfully", booking);
    }

    @GetMapping("/provider/bookings")
    public ApiResponse<PaginatedResponse<BookingResponse>> getAllBooking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponse<BookingResponse> bookings =
                bookingService.getAllBooking(page, size);

        return new ApiResponse<>(true, "Bookings fetched successfully", bookings);
    }

    @GetMapping("/provider/bookings/{id}")
    public ApiResponse<BookingResponse> getBookingById(@PathVariable Long id) {
        BookingResponse booking = bookingService.getBookingById(id);
        return new ApiResponse<>(true, "Booking fetched successfully", booking);
    }
}

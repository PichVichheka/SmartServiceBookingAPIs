package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.BookingRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.BookingResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);
//    List<BookingResponse> getallBooking();
    BookingResponse getBookingById(Long id);

    PaginatedResponse<BookingResponse> getAllBooking(int page, int size);
}

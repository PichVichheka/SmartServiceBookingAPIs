package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.BookingReqest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.BookingResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingReqest request);
//    List<BookingResponse> getallBooking();
    BookingResponse getBookingById(Long id);

    PaginatedResponse<BookingResponse> getAllBooking(int page, int size);
}

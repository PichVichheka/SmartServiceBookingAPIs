package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.BookingReqest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingReqest reqest);
    List<BookingResponse> getallBooking();
    BookingResponse getBookingById(Long id);

}

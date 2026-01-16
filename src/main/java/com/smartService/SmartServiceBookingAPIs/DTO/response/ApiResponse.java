package com.smartService.SmartServiceBookingAPIs.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;

    // Optional convenience constructors
    public ApiResponse(String message) {
        this.message = message;
    }

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}

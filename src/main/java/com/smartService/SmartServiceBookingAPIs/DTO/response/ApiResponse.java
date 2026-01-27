package com.smartService.SmartServiceBookingAPIs.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.swing.plaf.ActionMapUIResource;

@Data
@RequiredArgsConstructor
public class ApiResponse<T> {

    private Boolean success;
    private String message;
    private T data;

    // Optional convenience constructors
    public ApiResponse(String message) {
        this.message = message;
    }

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}

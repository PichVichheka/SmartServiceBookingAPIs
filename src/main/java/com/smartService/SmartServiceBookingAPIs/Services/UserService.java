package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.UserCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.UserUpdateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.UserResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;

import java.util.List;

public interface UserService {

    List<Users> getAll();

    UserResponse getById(Long id);

    UserResponse create(UserCreateRequest request);

    UserResponse update(UserUpdateRequest request, Long id);

    void delete(Long id);
}

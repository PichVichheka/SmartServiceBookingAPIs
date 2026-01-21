package com.smartService.SmartServiceBookingAPIs.Services;

import com.smartService.SmartServiceBookingAPIs.DTO.request.UserCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.UserUpdateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.UserResponse;

public interface UserService {

    PaginatedResponse<UserResponse> getAll(int page, int size);
//    List<Users> getAll();

    UserResponse getById(Long id);

    UserResponse create(UserCreateRequest request);

    UserResponse update(UserUpdateRequest request, Long id);

    void delete(Long id);
}

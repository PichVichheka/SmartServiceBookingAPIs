package com.smartService.SmartServiceBookingAPIs.Controllers;

import com.smartService.SmartServiceBookingAPIs.DTO.request.UserCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.UserUpdateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ApiResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.UserResponse;
import com.smartService.SmartServiceBookingAPIs.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserControllers {

    private final UserService userService;

    @GetMapping
    public ApiResponse<?> getAllUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        PaginatedResponse<UserResponse> paginatedUsers = userService.getAll(page - 1, size);
        return new ApiResponse<>(
                true,
                "Get users successfully.",
                paginatedUsers
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse getUserId = userService.getById(id);

        return new ApiResponse<>(
                true,
                "Get user successfully.",
                getUserId
        );
    }

    @PostMapping
    public ApiResponse<UserResponse> createdUser(
            @RequestBody UserCreateRequest request
            ) {
        UserResponse createdUser = userService.create(request);

        return new ApiResponse<>(
                true,
                "Created user successfully.",
                createdUser
        );
    }

    @PostMapping("/{id}")
    public ApiResponse<UserResponse> updatedUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request
            ) {
        UserResponse updatedUser = userService.update(request, id);

        return new ApiResponse<>(
                true,
                "User updated successfully.",
                updatedUser
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deletedUser(@PathVariable Long id) {
        userService.delete(id);

        return new ApiResponse<>(
                true,
                "User deleted successfully"
        );
    }

}

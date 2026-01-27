package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.UserCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.UserUpdateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.UserResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Roles;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import com.smartService.SmartServiceBookingAPIs.Services.UserService;
import com.smartService.SmartServiceBookingAPIs.Utils.HelperFunction;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.notFound;

import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HelperFunction helperFunction;

    @Override
    public PaginatedResponse<UserResponse> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Users> usersPage = userRepository.findAll(pageable);

        // Manual mapping: Users -> UserResponse
        List<UserResponse> userResponses = usersPage.getContent()
                .stream()
                .map(user -> {
                    UserResponse response = new UserResponse();
                    response.setId(user.getId());
                    response.setFullname(user.getFullname());
                    response.setUsername(user.getUsername());
                    response.setEmail(user.getEmail());
                    response.setPhone(user.getPhone());
                    if (user.getRoles() != null) {
                        response.setRoles(user.getRoles()
                                .stream()
                                .map(Roles::getName)
                                .toList());
                    }
                    return response;
                })
                .toList();

        PaginatedResponse.PaginationMeta paginationMeta =
                new PaginatedResponse.PaginationMeta(
                        usersPage.getNumber() + 1,
                        usersPage.getSize(),
                        usersPage.getTotalElements(),
                        usersPage.getTotalPages(),
                        usersPage.hasNext(),
                        usersPage.hasPrevious()
                );

        return new PaginatedResponse<>(userResponses, paginationMeta);
    }

    @Override
    public UserResponse getById(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() -> notFound("User not found."));

        // Manual mapping: Users -> UserResponse
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullname(user.getFullname());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        if (user.getRoles() != null) {
            response.setRoles(user.getRoles()
                    .stream()
                    .map(Roles::getName)
                    .toList());
        }

        return response;
    }

    @Override
    public UserResponse create(UserCreateRequest request) {

        helperFunction.validateCreate(request);

        // Manual mapping: UserCreateRequest -> Users
        Users user = new Users();
        user.setFullname(request.getFullname());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Users savedUser = userRepository.save(user);

        // Manual mapping: Users -> UserResponse
        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setFullname(savedUser.getFullname());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setPhone(savedUser.getPhone());
        if (savedUser.getRoles() != null) {
            response.setRoles(savedUser.getRoles()
                    .stream()
                    .map(Roles::getName)
                    .toList());
        }

        return response;
    }

    @Override
    public UserResponse update(UserUpdateRequest request, Long id) {

        Users updatedUser = userRepository.findById(id)
                .orElseThrow(() -> notFound("User not found, Update failed."));

        // Manual mapping: UserUpdateRequest -> Users (update existing entity)
        if (request.getFullname() != null) {
            updatedUser.setFullname(request.getFullname());
        }
        if (request.getPhone() != null) {
            updatedUser.setPhone(request.getPhone());
        }

        Users userUpdate = userRepository.save(updatedUser);

        // Manual mapping: Users -> UserResponse
        UserResponse response = new UserResponse();
        response.setId(userUpdate.getId());
        response.setFullname(userUpdate.getFullname());
        response.setUsername(userUpdate.getUsername());
        response.setEmail(userUpdate.getEmail());
        response.setPhone(userUpdate.getPhone());
        if (userUpdate.getRoles() != null) {
            response.setRoles(userUpdate.getRoles()
                    .stream()
                    .map(Roles::getName)
                    .toList());
        }

        return response;
    }

    @Override
    public void delete(Long id) {

        Users deletedUser = userRepository.findById(id)
                .orElseThrow(() -> notFound("User not found, Delete failed."));

        userRepository.delete(deletedUser);
    }
}
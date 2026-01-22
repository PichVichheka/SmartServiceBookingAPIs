package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.UserCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.UserUpdateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.UserResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Mapper.MapperFunction;
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
    private final MapperFunction mapperFunction;

    @Override
    public PaginatedResponse<UserResponse> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Users> usersPage = userRepository.findAll(pageable);

        List<UserResponse> userResponses = usersPage.getContent()
                .stream()
                .map(mapperFunction::toUserResponse)
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

        return mapperFunction.toUserResponse(user);
    }

    @Override
    public UserResponse create(UserCreateRequest request) {

        helperFunction.validateCreate(request);

        Users user = mapperFunction.toUsers(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Users savedUser = userRepository.save(user);

        return mapperFunction.toUserResponse(savedUser);
    }

    @Override
    public UserResponse update(UserUpdateRequest request, Long id) {

        Users updatedUser = userRepository.findById(id)
                .orElseThrow(() -> notFound("User not found, Update failed."));

        mapperFunction.updateUser(request, updatedUser);
        Users userUpdate = userRepository.save(updatedUser);

        return mapperFunction.toUserResponse(userUpdate);
    }

    @Override
    public void delete(Long id) {

        Users deletedUser = userRepository.findById(id)
                .orElseThrow(() -> notFound("User not found, Delete failed."));
    }
}

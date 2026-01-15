package com.smartService.SmartServiceBookingAPIs.Mapper;

import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.UserCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.UserUpdateRequest;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MapperFunction {

    // =========================
    // USER REQUEST
    // =========================
    Users toUsers(UserCreateRequest request);

    void updateUser(
            UserUpdateRequest request,
            @MappingTarget Users users
    );

    // ==============
    // REGISTER REQUEST MAPPINGS
    // ==============
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Users toUsers(RegisterRequest request);
}

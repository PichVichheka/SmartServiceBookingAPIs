package com.smartService.SmartServiceBookingAPIs.Mapper;

import com.smartService.SmartServiceBookingAPIs.DTO.request.RegisterRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.UserCreateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.request.UserUpdateRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.UserResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Roles;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface MapperFunction {

    // =========================
    // CREATE USER
    // =========================
    Users toUsers(UserCreateRequest request);

    // =========================
    // UPDATE USER
    // =========================
    void updateUser(
            UserUpdateRequest request,
            @MappingTarget Users users
    );

    // =========================
    // REGISTER → USERS
    // =========================
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "fullname", source = "fullname")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phone")
    Users toUsers(RegisterRequest request); // same as before


    // =========================
    // USER RESPONSE
    // =========================
    @Mapping(target = "roles", source = "roles")
    UserResponse toUserResponse(Users user);

    // =========================
    // ROLES
    // =========================
    default List<String> mapRoles(Set<Roles> roles) {
        if (roles == null) return List.of();
        return roles.stream()
                .map(Roles::getName)
                .toList();
    }
}

package com.smartService.SmartServiceBookingAPIs.DTO.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.smartService.SmartServiceBookingAPIs.Entity.Roles;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@JsonPropertyOrder({
        "id",
        "fullname",
        "username",
        "phone",
        "email",
        "roles"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullname;
    private String username;
    private String email;
    private String phone;
    private List<String> roles;

    public UserResponse(Users users) {
        this.id = users.getId();
        this.fullname = users.getFullname();
        this.username = users.getUsername();
        this.email = users.getEmail();
        this.phone = users.getPhone();
        this.roles = users.getRoles()
                .stream()
                .map(Roles::getName)
                .collect(Collectors.toList());

    }
}
package com.sivalabs.devzone.application.port.in;

import com.sivalabs.devzone.domain.Role;
import com.sivalabs.devzone.domain.User;

public record UserResponse(Long id, String name, String email, Role role) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                Role.valueOf(user.getRole().name()));
    }
}

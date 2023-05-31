package com.sivalabs.devzone.users.models;

import com.sivalabs.devzone.users.entities.RoleEnum;
import com.sivalabs.devzone.users.entities.User;

public record UserDTO(Long id, String name, String email, RoleEnum role) {

    public static UserDTO fromEntity(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}

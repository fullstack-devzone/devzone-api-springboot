package com.sivalabs.devzone.users.models;

import com.sivalabs.devzone.users.entities.Role;

public record AuthUserDTO(String name, String email, Role role) {}

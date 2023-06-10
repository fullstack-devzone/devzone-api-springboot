package com.sivalabs.devzone.application.port.in;

import com.sivalabs.devzone.domain.Role;

public record RegisterUserResponse(Long id, String name, String email, Role role) {}

package com.sivalabs.devzone.application.port.in;

import com.sivalabs.devzone.domain.Role;

public record AuthUserResponse(String name, String email, Role role) {}

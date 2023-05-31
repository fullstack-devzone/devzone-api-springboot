package com.sivalabs.devzone.users.models;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @NotBlank(message = "UserName cannot be blank") String username,
        @NotBlank(message = "Password cannot be blank") String password) {}

package com.sivalabs.devzone.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "UserName cannot be blank") String username,
        @NotBlank(message = "Password cannot be blank") String password) {}

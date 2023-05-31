package com.sivalabs.devzone.users.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sivalabs.devzone.users.entities.RoleEnum;

public record AuthUserDTO(
        @JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("role") RoleEnum role) {}

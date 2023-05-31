package com.sivalabs.devzone.users.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record AuthenticationResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_at") LocalDateTime expiresAt,
        @JsonProperty("user") AuthUserDTO user) {}

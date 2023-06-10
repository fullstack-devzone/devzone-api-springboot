package com.sivalabs.devzone.application.port.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record LoginResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_at") LocalDateTime expiresAt,
        @JsonProperty("user") AuthUserResponse user) {}

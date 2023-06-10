package com.sivalabs.devzone.adapter.in.web;

import com.sivalabs.devzone.application.port.in.AuthUserResponse;
import com.sivalabs.devzone.application.port.in.GetCurrentUserUseCase;
import com.sivalabs.devzone.common.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.domain.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
class GetCurrentUserController {
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    @GetMapping("/me")
    @AnyAuthenticatedUser
    public ResponseEntity<AuthUserResponse> me() {
        User loginUser = getCurrentUserUseCase.getCurrentUser().orElse(null);
        if (loginUser != null) {
            AuthUserResponse user =
                    new AuthUserResponse(loginUser.getName(), loginUser.getEmail(), loginUser.getRole());
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

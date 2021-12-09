package com.sivalabs.devzone.web.controllers.auth;

import com.sivalabs.devzone.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.annotations.CurrentUser;
import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.domain.entities.Role;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.models.AuthUserResponse;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GetCurrentUserController {

    @GetMapping("/auth/me")
    @AnyAuthenticatedUser
    public ResponseEntity<AuthUserResponse> me(@CurrentUser SecurityUser loginUser) {
        User user = loginUser.getUser();
        return ResponseEntity.ok(this.getAuthUserResponse(user));
    }

    private AuthUserResponse getAuthUserResponse(User user) {
        return AuthUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .build();
    }
}

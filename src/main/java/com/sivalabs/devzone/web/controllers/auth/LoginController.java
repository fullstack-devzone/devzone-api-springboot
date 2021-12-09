package com.sivalabs.devzone.web.controllers.auth;

import com.sivalabs.devzone.config.ApplicationProperties;
import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.config.security.TokenHelper;
import com.sivalabs.devzone.domain.entities.Role;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.models.AuthUserResponse;
import com.sivalabs.devzone.domain.models.AuthenticationRequest;
import com.sivalabs.devzone.domain.models.AuthenticationResponse;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final TokenHelper tokenHelper;
    private final ApplicationProperties applicationProperties;

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
            @RequestBody AuthenticationRequest credentials) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    credentials.getUsername(), credentials.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            SecurityUser user = (SecurityUser) authentication.getPrincipal();
            String accessToken = tokenHelper.generateToken(user.getUsername());
            return ResponseEntity.ok(getAuthenticationResponse(user, accessToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private AuthenticationResponse getAuthenticationResponse(SecurityUser loginUser, String token) {
        User user = loginUser.getUser();
        return AuthenticationResponse.builder()
                .user(this.getAuthUserResponse(user))
                .accessToken(token)
                .expiresAt(
                        LocalDateTime.now()
                                .plusSeconds(applicationProperties.getJwt().getExpiresIn()))
                .build();
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

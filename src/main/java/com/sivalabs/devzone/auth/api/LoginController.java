package com.sivalabs.devzone.auth.api;

import com.sivalabs.devzone.ApplicationProperties;
import com.sivalabs.devzone.auth.TokenHelper;
import com.sivalabs.devzone.users.domain.LoginRequest;
import com.sivalabs.devzone.users.domain.LoginResponse;
import com.sivalabs.devzone.users.domain.SecurityUser;
import com.sivalabs.devzone.users.domain.UserDTO;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class LoginController {
    private final AuthenticationManager authManager;
    private final TokenHelper tokenHelper;
    private final ApplicationProperties properties;

    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest credentials) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            SecurityUser user = (SecurityUser) authentication.getPrincipal();
            String accessToken = tokenHelper.generateToken(user.getUsername());
            return ResponseEntity.ok(getLoginResponse(user, accessToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private LoginResponse getLoginResponse(SecurityUser user, String token) {
        return new LoginResponse(
                token,
                LocalDateTime.now().plusSeconds(properties.getJwt().getExpiresIn()),
                new UserDTO(user.getUserId(), user.getName(), user.getUsername(), user.getRole()));
    }
}

package com.sivalabs.devzone.users.api;

import com.sivalabs.devzone.ApplicationProperties;
import com.sivalabs.devzone.security.SecurityUser;
import com.sivalabs.devzone.security.TokenHelper;
import com.sivalabs.devzone.users.domain.AuthUserDTO;
import com.sivalabs.devzone.users.domain.LoginRequest;
import com.sivalabs.devzone.users.domain.LoginResponse;
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
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final TokenHelper tokenHelper;
    private final ApplicationProperties applicationProperties;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest credentials) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            SecurityUser user = (SecurityUser) authentication.getPrincipal();
            String accessToken = tokenHelper.generateToken(user.getUsername());
            return ResponseEntity.ok(getAuthenticationResponse(user, accessToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private LoginResponse getAuthenticationResponse(SecurityUser user, String token) {
        return new LoginResponse(
                token,
                LocalDateTime.now().plusSeconds(applicationProperties.getJwt().getExpiresIn()),
                new AuthUserDTO(
                        user.getUser().getName(),
                        user.getUser().getEmail(),
                        user.getUser().getRole()));
    }
}

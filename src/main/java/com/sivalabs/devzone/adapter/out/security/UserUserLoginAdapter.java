package com.sivalabs.devzone.adapter.out.security;

import com.sivalabs.devzone.application.port.in.AuthUserResponse;
import com.sivalabs.devzone.application.port.in.LoginRequest;
import com.sivalabs.devzone.application.port.in.LoginResponse;
import com.sivalabs.devzone.application.port.out.UserLoginPort;
import com.sivalabs.devzone.config.ApplicationProperties;
import com.sivalabs.devzone.config.security.TokenHelper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserUserLoginAdapter implements UserLoginPort {
    private final AuthenticationManager authenticationManager;
    private final TokenHelper tokenHelper;
    private final ApplicationProperties applicationProperties;

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
            var authentication = authenticationManager.authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            SecurityUser user = (SecurityUser) authentication.getPrincipal();
            String accessToken = tokenHelper.generateToken(user.getUsername());
            return getAuthenticationResponse(user, accessToken);
        } catch (Exception e) {
            return null;
        }
    }

    private LoginResponse getAuthenticationResponse(SecurityUser user, String token) {
        return new LoginResponse(
                token,
                LocalDateTime.now().plusSeconds(applicationProperties.getJwt().getExpiresIn()),
                new AuthUserResponse(
                        user.getUser().getName(),
                        user.getUser().getEmail(),
                        user.getUser().getRole()));
    }
}

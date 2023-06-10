package com.sivalabs.devzone.adapter.out.security;

import com.sivalabs.devzone.application.port.out.GetCurrentUserPort;
import com.sivalabs.devzone.application.port.out.UserRepository;
import com.sivalabs.devzone.domain.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class SecurityAdapter implements GetCurrentUserPort {
    private final UserRepository userRepository;

    @Override
    public Optional<User> getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser securityUser) {
            return Optional.of(securityUser.getUser());
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userRepository.findByEmail(userDetails.getUsername());
        }
        return Optional.empty();
    }
}

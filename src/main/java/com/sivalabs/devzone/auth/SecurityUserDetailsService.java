package com.sivalabs.devzone.auth;

import com.sivalabs.devzone.users.domain.SecurityUser;
import com.sivalabs.devzone.users.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class SecurityUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userService
                .getUserByEmail(username)
                .map(user -> new SecurityUser(user.id(), user.name(), user.email(), user.password(), user.role()))
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
    }
}

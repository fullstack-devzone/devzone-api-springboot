package com.sivalabs.devzone.config.security;

import com.sivalabs.devzone.adapter.out.security.SecurityUser;
import com.sivalabs.devzone.application.port.in.FindUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final FindUserUseCase findUserUseCase;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return findUserUseCase
                .findByEmail(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
    }
}

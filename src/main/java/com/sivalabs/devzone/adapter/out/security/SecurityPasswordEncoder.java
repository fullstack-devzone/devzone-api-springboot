package com.sivalabs.devzone.adapter.out.security;

import com.sivalabs.devzone.application.port.out.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class SecurityPasswordEncoder implements PasswordEncoderPort {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String plainText) {
        return passwordEncoder.encode(plainText);
    }
}

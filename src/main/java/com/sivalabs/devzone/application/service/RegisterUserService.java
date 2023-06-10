package com.sivalabs.devzone.application.service;

import com.sivalabs.devzone.application.port.in.RegisterUserRequest;
import com.sivalabs.devzone.application.port.in.RegisterUserUseCase;
import com.sivalabs.devzone.application.port.out.PasswordEncoderPort;
import com.sivalabs.devzone.application.port.out.UserRepository;
import com.sivalabs.devzone.common.exceptions.BadRequestException;
import com.sivalabs.devzone.domain.Role;
import com.sivalabs.devzone.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class RegisterUserService implements RegisterUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public User register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email " + request.email() + " is already in use");
        }
        String encPwd = passwordEncoderPort.encode(request.password());
        User user = new User(request.name(), request.email(), encPwd, Role.ROLE_USER);
        return userRepository.save(user);
    }
}

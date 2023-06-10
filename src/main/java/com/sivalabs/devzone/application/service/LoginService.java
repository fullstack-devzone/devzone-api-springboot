package com.sivalabs.devzone.application.service;

import com.sivalabs.devzone.application.port.in.LoginRequest;
import com.sivalabs.devzone.application.port.in.LoginResponse;
import com.sivalabs.devzone.application.port.in.LoginUseCase;
import com.sivalabs.devzone.application.port.out.UserLoginPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class LoginService implements LoginUseCase {
    private final UserLoginPort userLoginPort;

    @Override
    public LoginResponse login(LoginRequest request) {
        return userLoginPort.login(request);
    }
}

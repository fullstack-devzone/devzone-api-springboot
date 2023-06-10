package com.sivalabs.devzone.application.port.in;

import com.sivalabs.devzone.domain.User;

public interface RegisterUserUseCase {
    User register(RegisterUserRequest request);
}

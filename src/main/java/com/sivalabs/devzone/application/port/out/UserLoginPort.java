package com.sivalabs.devzone.application.port.out;

import com.sivalabs.devzone.application.port.in.LoginRequest;
import com.sivalabs.devzone.application.port.in.LoginResponse;

public interface UserLoginPort {
    LoginResponse login(LoginRequest request);
}

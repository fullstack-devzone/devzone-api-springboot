package com.sivalabs.devzone.adapter.in.web;

import static org.springframework.http.HttpStatus.CREATED;

import com.sivalabs.devzone.application.port.in.RegisterUserRequest;
import com.sivalabs.devzone.application.port.in.RegisterUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
class UserRegistrationController {
    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping
    @ResponseStatus(CREATED)
    public void createUser(@RequestBody @Valid RegisterUserRequest request) {
        log.info("process=create_user, user_email={}", request.email());
        registerUserUseCase.register(request);
    }
}

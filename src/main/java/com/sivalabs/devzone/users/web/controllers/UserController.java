package com.sivalabs.devzone.users.web.controllers;

import static org.springframework.http.HttpStatus.CREATED;

import com.sivalabs.devzone.users.models.CreateUserRequest;
import com.sivalabs.devzone.users.models.UserDTO;
import com.sivalabs.devzone.users.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.info("process=get_user, user_id={}", id);
        return userService
                .getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public UserDTO createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        log.info("process=create_user, user_email={}", createUserRequest.email());
        return userService.createUser(createUserRequest);
    }
}

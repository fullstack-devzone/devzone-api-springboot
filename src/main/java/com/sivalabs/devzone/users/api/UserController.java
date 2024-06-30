package com.sivalabs.devzone.users.api;

import static org.springframework.http.HttpStatus.CREATED;

import com.sivalabs.devzone.users.domain.CreateUserRequest;
import com.sivalabs.devzone.users.domain.UserDTO;
import com.sivalabs.devzone.users.domain.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.info("Get user by id={}", id);
        return userService
                .getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    UserDTO createUser(@RequestBody @Valid CreateUserRequest request) {
        log.info("Create user with email={}", request.email());
        return userService.createUser(request);
    }
}

package com.sivalabs.devzone.web.controllers;

import static org.springframework.http.HttpStatus.CREATED;

import com.sivalabs.devzone.domain.models.CreateUserRequest;
import com.sivalabs.devzone.domain.models.UserDTO;
import com.sivalabs.devzone.domain.services.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserRestController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("process=get_user_by_id, user_id=" + id);
        return userService
                .getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public UserDTO createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        log.info("process=create_user, user_email=" + createUserRequest.getEmail());
        UserDTO userDTO = new UserDTO();
        userDTO.setName(createUserRequest.getName());
        userDTO.setEmail(createUserRequest.getEmail());
        userDTO.setPassword(createUserRequest.getPassword());
        return userService.createUser(userDTO);
    }
}

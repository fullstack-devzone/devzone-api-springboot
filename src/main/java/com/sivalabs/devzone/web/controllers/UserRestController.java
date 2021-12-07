package com.sivalabs.devzone.web.controllers;

import static org.springframework.http.HttpStatus.CREATED;

import com.sivalabs.devzone.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.domain.models.CreateUserRequest;
import com.sivalabs.devzone.domain.models.UserDTO;
import com.sivalabs.devzone.domain.services.SecurityService;
import com.sivalabs.devzone.domain.services.UserService;
import com.sivalabs.devzone.web.exceptions.BadRequestException;
import java.util.List;
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
    private final SecurityService securityService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("process=get_all_user");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.info("process=get_user, user_id=" + id);
        return userService
                .getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @ResponseStatus(CREATED)
    public UserDTO createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        log.info("process=create_user, user_email=" + createUserRequest.getEmail());
        UserDTO userDTO = new UserDTO();
        userDTO.setName(createUserRequest.getName());
        userDTO.setEmail(createUserRequest.getEmail());
        userDTO.setPassword(createUserRequest.getPassword());
        return userService.createUser(userDTO);
    }

    @DeleteMapping("/{id}")
    @AnyAuthenticatedUser
    public void deleteUser(@PathVariable Long id) {
        log.info("process=delete_user, user_id=" + id);
        UserDTO userDTO =
                userService
                        .getUserById(id)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "User not found with id=" + id));
        if (!userDTO.getId().equals(securityService.loginUserId())
                && !securityService.isCurrentUserAdmin()) {
            throw new BadRequestException("Bad request to delete User with id=" + id);
        }
        userService.deleteUser(id);
    }
}

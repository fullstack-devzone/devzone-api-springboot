package com.sivalabs.devzone.web.controllers.users;

import static org.springframework.http.HttpStatus.CREATED;

import com.sivalabs.devzone.domain.models.CreateUserRequest;
import com.sivalabs.devzone.domain.models.UserDTO;
import com.sivalabs.devzone.domain.services.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class CreateUserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    public UserDTO createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(createUserRequest.getName());
        userDTO.setEmail(createUserRequest.getEmail());
        userDTO.setPassword(createUserRequest.getPassword());
        return userService.createUser(userDTO);
    }
}

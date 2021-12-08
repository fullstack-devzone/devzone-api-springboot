package com.sivalabs.devzone.web.controllers;

import static org.springframework.http.HttpStatus.CREATED;

import com.sivalabs.devzone.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.domain.models.CreateUserRequest;
import com.sivalabs.devzone.domain.models.UserDTO;
import com.sivalabs.devzone.domain.services.SecurityService;
import com.sivalabs.devzone.domain.services.UserService;
import com.sivalabs.devzone.web.exceptions.BadRequestException;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping("/{id}/profile_pic")
    public ResponseEntity<Resource> getUserProfilePic(
            @PathVariable Long id, HttpServletRequest request) {
        Resource resource = userService.getUserImageUrl(id);
        if (resource == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String contentType = null;
        try {
            contentType =
                    request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

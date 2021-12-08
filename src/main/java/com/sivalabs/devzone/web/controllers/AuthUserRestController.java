package com.sivalabs.devzone.web.controllers;

import com.sivalabs.devzone.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.annotations.CurrentUser;
import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.domain.entities.Role;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.models.AuthUserResponse;
import com.sivalabs.devzone.domain.models.ChangePasswordRequest;
import com.sivalabs.devzone.domain.models.UpdateUserRequest;
import com.sivalabs.devzone.domain.models.UserDTO;
import com.sivalabs.devzone.domain.services.UserService;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@AnyAuthenticatedUser
@Slf4j
public class AuthUserRestController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<AuthUserResponse> me(@CurrentUser SecurityUser loginUser) {
        User user = loginUser.getUser();
        AuthUserResponse userDTO =
                AuthUserResponse.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .roles(
                                user.getRoles().stream()
                                        .map(Role::getName)
                                        .collect(Collectors.toList()))
                        .build();
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping
    public UserDTO updateUser(
            @RequestBody @Valid UpdateUserRequest updateUserRequest,
            @CurrentUser SecurityUser loginUser) {
        User user = loginUser.getUser();
        Long id = user.getId();
        log.info("process=update_user, user_id=" + id);
        updateUserRequest.setId(id);
        return userService.updateUser(updateUserRequest);
    }

    @PutMapping("/change-password")
    public void changePassword(
            @RequestBody @Valid ChangePasswordRequest changePasswordRequest,
            @CurrentUser SecurityUser loginUser) {
        String email = loginUser.getUser().getEmail();
        log.info("process=change_password, email=" + email);
        userService.changePassword(email, changePasswordRequest);
    }

    @PutMapping("/profile_pic")
    public Map<String, String> updateUserProfilePic(
            @RequestParam("file") MultipartFile file, @CurrentUser SecurityUser loginUser)
            throws IOException {
        Long id = loginUser.getUser().getId();
        String imageUrl = userService.updateUserProfilePic(id, file.getInputStream());
        log.info("File imageUrl: {}", imageUrl);
        String fileDownloadUri =
                ServletUriComponentsBuilder.fromCurrentContextPath().path(imageUrl).toUriString();
        log.info("fileDownloadUri: {}", fileDownloadUri);
        return Map.of("imageUrl", fileDownloadUri);
    }
}

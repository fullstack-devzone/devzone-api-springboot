package com.sivalabs.devzone.users.web.controllers;

import com.sivalabs.devzone.common.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.config.security.SecurityUtils;
import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.models.AuthUserDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class AuthUserController {
    private final SecurityUtils securityUtils;

    @GetMapping("/me")
    @AnyAuthenticatedUser
    public ResponseEntity<AuthUserDTO> me() {
        User loginUser = securityUtils.loginUser();
        if (loginUser != null) {
            AuthUserDTO userDTO = new AuthUserDTO(loginUser.getName(), loginUser.getEmail(), loginUser.getRole());
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

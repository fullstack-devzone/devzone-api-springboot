package com.sivalabs.devzone.users.api;

import com.sivalabs.devzone.users.domain.AuthUserDTO;
import com.sivalabs.devzone.users.domain.SecurityService;
import com.sivalabs.devzone.users.domain.User;
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
class CurrentUserController {

    @GetMapping("/me")
    ResponseEntity<AuthUserDTO> me() {
        try {
            User loginUser = SecurityService.getCurrentUserOrThrow();
            AuthUserDTO userDTO = new AuthUserDTO(loginUser.getName(), loginUser.getEmail(), loginUser.getRole());
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

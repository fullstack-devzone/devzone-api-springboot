package com.sivalabs.devzone.auth.api;

import com.sivalabs.devzone.auth.SecurityService;
import com.sivalabs.devzone.users.domain.SecurityUser;
import com.sivalabs.devzone.users.domain.UserDTO;
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
    ResponseEntity<UserDTO> me() {
        try {
            SecurityUser loginUser = SecurityService.getCurrentUserOrThrow();
            UserDTO userDTO = new UserDTO(
                    loginUser.getUserId(), loginUser.getName(), loginUser.getUsername(), loginUser.getRole());
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

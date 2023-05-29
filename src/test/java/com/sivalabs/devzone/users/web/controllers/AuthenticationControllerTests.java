package com.sivalabs.devzone.users.web.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import com.sivalabs.devzone.config.ApplicationProperties;
import com.sivalabs.devzone.config.security.TokenHelper;
import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.models.AuthenticationRequest;
import com.sivalabs.devzone.users.models.CreateUserRequest;
import com.sivalabs.devzone.users.models.UserDTO;
import com.sivalabs.devzone.users.services.UserService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

class AuthenticationControllerTests extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private ApplicationProperties properties;

    @Test
    void should_login_successfully_with_valid_credentials() throws Exception {
        User user = createUser();
        AuthenticationRequest authenticationRequestDTO = AuthenticationRequest.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .build();

        this.mockMvc
                .perform(post("/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void should_not_login_with_invalid_credentials() throws Exception {
        AuthenticationRequest authenticationRequestDTO = AuthenticationRequest.builder()
                .username("nonexisting@gmail.com")
                .password("secret")
                .build();

        this.mockMvc
                .perform(post("/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequestDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser("siva@gmail.com")
    void should_get_refreshed_authToken_if_authorized() throws Exception {
        String token = tokenHelper.generateToken("siva@gmail.com");
        this.mockMvc
                .perform(post("/api/refresh").header(properties.getJwt().getHeader(), "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void should_fail_to_get_refreshed_authToken_if_unauthorized() throws Exception {
        this.mockMvc.perform(post("/api/refresh")).andExpect(status().isForbidden());
    }

    @Test
    void should_fail_to_get_refreshed_authToken_if_token_is_invalid() throws Exception {
        this.mockMvc
                .perform(post("/api/refresh").header(properties.getJwt().getHeader(), "Bearer invalid-token"))
                .andExpect(status().isForbidden());
    }

    private User createUser() {
        String uuid = UUID.randomUUID().toString();
        CreateUserRequest request = new CreateUserRequest(uuid, uuid + "@gmail.com", uuid);
        UserDTO userDTO = userService.createUser(request);
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(userDTO.getRole());
        return user;
    }
}

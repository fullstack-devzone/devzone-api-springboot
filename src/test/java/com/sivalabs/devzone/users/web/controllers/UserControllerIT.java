package com.sivalabs.devzone.users.web.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import com.sivalabs.devzone.users.models.CreateUserRequest;
import org.junit.jupiter.api.Test;

class UserControllerIT extends AbstractIntegrationTest {

    @Test
    void should_find_user_by_id() throws Exception {
        Long userId = 1L;
        this.mockMvc.perform(get("/api/users/{id}", userId)).andExpect(status().isOk());
    }

    @Test
    void should_create_new_user_with_valid_data() throws Exception {
        CreateUserRequest createUserRequestDTO =
                CreateUserRequest.builder()
                        .email("myemail@gmail.com")
                        .password("secret")
                        .name("myname")
                        .build();

        this.mockMvc
                .perform(
                        post("/api/users")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createUserRequestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void should_fail_to_create_new_user_with_existing_email() throws Exception {
        CreateUserRequest createUserRequestDTO =
                CreateUserRequest.builder()
                        .email("admin@gmail.com")
                        .password("secret")
                        .name("myname")
                        .build();

        this.mockMvc
                .perform(
                        post("/api/users")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createUserRequestDTO)))
                .andExpect(status().isBadRequest());
    }
}

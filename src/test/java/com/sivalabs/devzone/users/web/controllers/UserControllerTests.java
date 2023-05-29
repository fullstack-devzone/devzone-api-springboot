package com.sivalabs.devzone.users.web.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import com.sivalabs.devzone.users.models.CreateUserRequest;
import org.junit.jupiter.api.Test;

class UserControllerTests extends AbstractIntegrationTest {

    @Test
    void should_find_user_by_id() throws Exception {
        Long userId = 1L;
        this.mockMvc.perform(get("/api/users/{id}", userId)).andExpect(status().isOk());
    }

    @Test
    void should_create_new_user_with_valid_data() throws Exception {
        CreateUserRequest createUserRequestDTO = new CreateUserRequest("myname", "myemail@gmail.com", "secret");

        this.mockMvc
                .perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void should_fail_to_create_new_user_with_existing_email() throws Exception {
        CreateUserRequest createUserRequestDTO = new CreateUserRequest("admin@gmail.com", "secret", "myname");

        this.mockMvc
                .perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequestDTO)))
                .andExpect(status().isBadRequest());
    }
}

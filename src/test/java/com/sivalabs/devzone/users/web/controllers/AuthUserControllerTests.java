package com.sivalabs.devzone.users.web.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

class AuthUserControllerTests extends AbstractIntegrationTest {

    @Test
    @WithMockUser("siva@gmail.com")
    void should_get_login_user_details() throws Exception {
        this.mockMvc.perform(get("/api/me")).andExpect(status().isOk());
    }

    @Test
    void should_fail_to_get_login_user_details_if_unauthorized() throws Exception {
        this.mockMvc.perform(get("/api/me")).andExpect(status().isForbidden());
    }
}

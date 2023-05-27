package com.sivalabs.devzone.posts.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.posts.models.PostsDTO;
import com.sivalabs.devzone.posts.services.PostService;
import com.sivalabs.devzone.users.services.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = PostController.class)
class PostControllerTest extends AbstractWebMvcTest {
    @MockBean
    protected PostService postService;

    @MockBean
    protected SecurityService securityService;

    @Test
    void shouldFetchPostsFirstPage() throws Exception {
        PostsDTO postsDTO = new PostsDTO();
        given(postService.getAllPosts(any(Integer.class))).willReturn(postsDTO);

        this.mockMvc.perform(get("/api/posts")).andExpect(status().isOk());
    }
}

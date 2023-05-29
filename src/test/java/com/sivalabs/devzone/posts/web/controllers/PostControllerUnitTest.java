package com.sivalabs.devzone.posts.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.common.models.PagedResult;
import com.sivalabs.devzone.posts.models.PostDTO;
import com.sivalabs.devzone.posts.services.PostService;
import com.sivalabs.devzone.users.services.SecurityService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = PostController.class)
class PostControllerUnitTest extends AbstractWebMvcTest {
    @MockBean
    protected PostService postService;

    @MockBean
    protected SecurityService securityService;

    @Test
    void shouldFetchPostsFirstPage() throws Exception {
        PagedResult<PostDTO> postsDTO = new PagedResult<>(List.of(), 10, 1, 1);
        given(postService.getAllPosts(any(Integer.class))).willReturn(postsDTO);

        this.mockMvc.perform(get("/api/posts")).andExpect(status().isOk());
    }
}

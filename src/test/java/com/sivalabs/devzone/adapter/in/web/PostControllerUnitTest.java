package com.sivalabs.devzone.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.adapter.in.web.PostController;
import com.sivalabs.devzone.application.port.in.GetCurrentUserUseCase;
import com.sivalabs.devzone.application.port.in.PostDTO;
import com.sivalabs.devzone.application.port.in.PostUseCase;
import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.domain.PagedResult;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = PostController.class)
class PostControllerUnitTest extends AbstractWebMvcTest {
    @MockBean
    protected PostUseCase postUseCase;

    @MockBean
    protected GetCurrentUserUseCase getCurrentUserUseCase;

    @Test
    void shouldFetchPostsFirstPage() throws Exception {
        PagedResult<PostDTO> postsDTO = new PagedResult<>(List.of(), 10, 1, 1);
        given(postUseCase.getAllPosts(any(Integer.class))).willReturn(postsDTO);

        this.mockMvc.perform(get("/api/posts")).andExpect(status().isOk());
    }
}

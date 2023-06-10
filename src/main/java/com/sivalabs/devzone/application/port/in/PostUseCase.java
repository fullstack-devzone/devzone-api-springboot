package com.sivalabs.devzone.application.port.in;

import com.sivalabs.devzone.domain.PagedResult;
import java.util.Optional;

public interface PostUseCase {
    PagedResult<PostDTO> getAllPosts(int page);

    PagedResult<PostDTO> searchPosts(String query, int page);

    Optional<PostDTO> getPostById(Long id);

    PostDTO createPost(CreatePostRequest request);

    void deletePost(Long id);

    void deleteAllPosts();
}

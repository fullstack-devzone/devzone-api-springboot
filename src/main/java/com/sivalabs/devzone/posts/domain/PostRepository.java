package com.sivalabs.devzone.posts.domain;

import com.sivalabs.devzone.common.models.PagedResult;
import java.util.Optional;

public interface PostRepository {
    PagedResult<PostDTO> getPosts(int page, int size);

    PagedResult<PostDTO> searchPosts(String query, int page, int size);

    Optional<PostDTO> findById(Long id);

    PostDTO save(Post post);

    void deleteById(Long id);

    void deleteAll();
}

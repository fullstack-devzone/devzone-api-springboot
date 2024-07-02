package com.sivalabs.devzone.posts.domain;

import com.sivalabs.devzone.common.models.PagedResult;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private static final int PAGE_SIZE = 10;

    private final PostRepository postRepository;

    public PagedResult<PostDTO> getAllPosts(int page) {
        return postRepository.getAllPosts(page, PAGE_SIZE);
    }

    public PagedResult<PostDTO> searchPosts(String query, int page) {
        return postRepository.searchPosts(query, page, PAGE_SIZE);
    }

    public Optional<PostDTO> getPostById(Long id) {
        log.debug("get post by id={}", id);
        return postRepository.findById(id);
    }

    @Transactional
    public PostDTO createPost(CreatePostCmd createPostCmd) {
        log.debug("create post with url={}", createPostCmd.url());
        Post post = new Post(
                null,
                createPostCmd.url(),
                createPostCmd.title(),
                createPostCmd.content(),
                createPostCmd.userId(),
                Instant.now(),
                null);
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id) {
        log.debug("delete post by id={}", id);
        postRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllPosts() {
        log.debug("delete all posts");
        postRepository.deleteAll();
    }
}

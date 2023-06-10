package com.sivalabs.devzone.application.service;

import com.sivalabs.devzone.application.port.in.CreatePostRequest;
import com.sivalabs.devzone.application.port.in.PostDTO;
import com.sivalabs.devzone.application.port.in.PostUseCase;
import com.sivalabs.devzone.application.port.out.PostRepository;
import com.sivalabs.devzone.application.port.out.UserRepository;
import com.sivalabs.devzone.domain.PagedResult;
import com.sivalabs.devzone.domain.Post;
import com.sivalabs.devzone.domain.User;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService implements PostUseCase {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PagedResult<PostDTO> getAllPosts(int page) {
        Page<PostDTO> posts = postRepository.findAll(page).map(PostDTO::from);
        return new PagedResult<>(
                posts.getContent(), posts.getTotalElements(), posts.getNumber(), posts.getTotalPages());
    }

    @Transactional(readOnly = true)
    public PagedResult<PostDTO> searchPosts(String query, int page) {
        Page<PostDTO> posts = postRepository.searchByTitle(query, page).map(PostDTO::from);
        return new PagedResult<>(
                posts.getContent(), posts.getTotalElements(), posts.getNumber(), posts.getTotalPages());
    }

    @Transactional(readOnly = true)
    public Optional<PostDTO> getPostById(Long id) {
        log.debug("get post by id={}", id);
        return postRepository.findById(id).map(PostDTO::from);
    }

    public PostDTO createPost(CreatePostRequest request) {
        User user = userRepository.findById(request.userId()).orElseThrow();
        Post post = new Post(null, request.title(), request.url(), request.content(), user, LocalDateTime.now(), null);
        log.debug("create post with url={}", post.getUrl());
        Post savedPost = postRepository.save(post);
        return PostDTO.from(savedPost);
    }

    public void deletePost(Long id) {
        log.debug("delete post by id={}", id);
        postRepository.deleteById(id);
    }

    public void deleteAllPosts() {
        log.debug("delete all posts");
        postRepository.deleteAll();
    }
}

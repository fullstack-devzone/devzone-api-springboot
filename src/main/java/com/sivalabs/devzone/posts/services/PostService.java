package com.sivalabs.devzone.posts.services;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.sivalabs.devzone.common.models.PagedResult;
import com.sivalabs.devzone.posts.entities.Post;
import com.sivalabs.devzone.posts.models.CreatePostRequest;
import com.sivalabs.devzone.posts.models.PostDTO;
import com.sivalabs.devzone.posts.repositories.PostRepository;
import com.sivalabs.devzone.users.repositories.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private static final int PAGE_SIZE = 10;

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PagedResult<PostDTO> getAllPosts(int page) {
        Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, PAGE_SIZE, DESC, "createdAt");
        Page<PostDTO> pageOfPosts = postRepository.findAll(pageable).map(PostDTO::from);
        return new PagedResult<>(
                pageOfPosts.getContent(), pageOfPosts.getTotalElements(), page, pageOfPosts.getTotalPages());
    }

    @Transactional(readOnly = true)
    public PagedResult<PostDTO> searchPosts(String query, int page) {
        Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, PAGE_SIZE, DESC, "createdAt");
        Page<PostDTO> pageOfPosts =
                postRepository.searchByTitle("%" + query + "%", pageable).map(PostDTO::from);
        return new PagedResult<>(
                pageOfPosts.getContent(), pageOfPosts.getTotalElements(), page, pageOfPosts.getTotalPages());
    }

    @Transactional(readOnly = true)
    public Optional<PostDTO> getPostById(Long id) {
        log.debug("get post by id={}", id);
        return postRepository.findById(id).map(PostDTO::from);
    }

    public PostDTO createPost(CreatePostRequest createPostRequest) {
        Post post = new Post();
        post.setUrl(createPostRequest.url());
        post.setTitle(createPostRequest.title());
        post.setContent(createPostRequest.content());
        post.setCreatedBy(userRepository.getReferenceById(createPostRequest.userId()));

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
        postRepository.deleteAllInBatch();
    }
}

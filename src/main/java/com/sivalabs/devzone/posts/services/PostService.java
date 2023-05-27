package com.sivalabs.devzone.posts.services;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.sivalabs.devzone.posts.entities.Post;
import com.sivalabs.devzone.posts.mappers.PostMapper;
import com.sivalabs.devzone.posts.models.PostDTO;
import com.sivalabs.devzone.posts.models.PostsDTO;
import com.sivalabs.devzone.posts.repositories.PostRepository;
import com.sivalabs.devzone.users.repositories.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public PostsDTO getAllPosts(int page) {
        Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, PAGE_SIZE, DESC, "createdAt");
        Page<Long> pageOfPostIds = postRepository.fetchPostIds(pageable);
        List<Post> posts = postRepository.findPosts(pageOfPostIds.getContent(), pageable.getSort());
        Page<Post> pageOfPosts = new PageImpl<>(posts, pageable, pageOfPostIds.getTotalElements());
        return buildPostsResult(pageOfPosts);
    }

    @Transactional(readOnly = true)
    public PostsDTO searchPosts(String query, int page) {
        Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, PAGE_SIZE, DESC, "createdAt");
        Page<Long> pageOfPostIds = postRepository.fetchPostIdsByTitleContainingIgnoreCase(query, pageable);
        List<Post> posts = postRepository.findPosts(pageOfPostIds.getContent(), pageable.getSort());
        Page<Post> pageOfPosts = new PageImpl<>(posts, pageable, pageOfPostIds.getTotalElements());
        return buildPostsResult(pageOfPosts);
    }

    @Transactional(readOnly = true)
    public Optional<PostDTO> getPostById(Long id) {
        log.debug("process=get_post_by_id, id={}", id);
        return postRepository.findById(id).map(postMapper::toDTO);
    }

    public PostDTO createPost(PostDTO post) {
        post.setId(null);
        log.debug("process=create_post, url={}", post.getUrl());
        return postMapper.toDTO(savePost(post));
    }

    public void deletePost(Long id) {
        log.debug("process=delete_post_by_id, id={}", id);
        postRepository.deleteById(id);
    }

    public void deleteAllPosts() {
        log.debug("process=delete_all_posts");
        postRepository.deleteAllInBatch();
    }

    private PostsDTO buildPostsResult(Page<Post> posts) {
        log.trace("Found {} posts in page", posts.getNumberOfElements());
        return new PostsDTO(posts.map(postMapper::toDTO));
    }

    private Post savePost(PostDTO postDTO) {
        Post post = new Post();
        if (postDTO.getId() != null) {
            post = postRepository.findById(postDTO.getId()).orElse(post);
        }
        post.setUrl(postDTO.getUrl());
        post.setTitle(postDTO.getTitle());
        post.setCreatedBy(userRepository.getReferenceById(postDTO.getCreatedUserId()));
        post.setCreatedAt(Instant.now());

        return postRepository.save(post);
    }
}

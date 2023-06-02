package com.sivalabs.devzone.posts.web.controllers;

import com.sivalabs.devzone.common.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.common.annotations.CurrentUser;
import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.common.exceptions.UnauthorisedAccessException;
import com.sivalabs.devzone.common.models.PagedResult;
import com.sivalabs.devzone.posts.models.CreatePostRequest;
import com.sivalabs.devzone.posts.models.PostDTO;
import com.sivalabs.devzone.posts.services.PostService;
import com.sivalabs.devzone.users.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @GetMapping
    public PagedResult<PostDTO> getPosts(
            @RequestParam(name = "query", defaultValue = "") String query,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        if (StringUtils.isNotEmpty(query)) {
            log.info("Searching posts for {} with page: {}", query, page);
            return postService.searchPosts(query, page);
        } else {
            log.info("Fetching posts with page: {}", page);
            return postService.getAllPosts(page);
        }
    }

    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable Long id) {
        return postService
                .getPostById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id: " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AnyAuthenticatedUser
    @Operation(summary = "Create Post", security = @SecurityRequirement(name = "bearerAuth"))
    public PostDTO createPost(@Valid @RequestBody CreatePostRequest createPostRequest, @CurrentUser User loginUser) {
        CreatePostRequest request = new CreatePostRequest(
                createPostRequest.title(), createPostRequest.url(), createPostRequest.content(), loginUser.getId());
        return postService.createPost(request);
    }

    @DeleteMapping("/{id}")
    @AnyAuthenticatedUser
    @Operation(summary = "Delete Post", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @CurrentUser User loginUser) {
        PostDTO post = postService.getPostById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        this.checkPrivilege(post, loginUser);
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    private void checkPrivilege(PostDTO post, User loginUser) {
        if (!(post.getCreatedBy().getId().equals(loginUser.getId()) || loginUser.isCurrentUserAdmin())) {
            throw new UnauthorisedAccessException("Unauthorised Access");
        }
    }
}
package com.sivalabs.devzone.posts.api;

import com.sivalabs.devzone.auth.SecurityService;
import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.common.exceptions.UnauthorisedAccessException;
import com.sivalabs.devzone.common.models.PagedResult;
import com.sivalabs.devzone.posts.domain.CreatePostCmd;
import com.sivalabs.devzone.posts.domain.PostDTO;
import com.sivalabs.devzone.posts.domain.PostService;
import com.sivalabs.devzone.users.domain.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
class PostController {
    private final PostService postService;

    @GetMapping
    PagedResult<PostDTO> getPosts(
            @RequestParam(name = "query", defaultValue = "") String query,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        if (StringUtils.isNotEmpty(query)) {
            log.info("Searching posts for {} with page: {}", query, page);
            return postService.searchPosts(query, page);
        } else {
            log.info("Fetching posts with page: {}", page);
            return postService.getPosts(page);
        }
    }

    @GetMapping("/{id}")
    PostDTO getPost(@PathVariable Long id) {
        return postService
                .getPostById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id: " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Post", security = @SecurityRequirement(name = "bearerAuth"))
    PostDTO createPost(@Valid @RequestBody CreatePostRequestPayload payload) {
        Long loginUserId = SecurityService.loginUserId();
        CreatePostCmd request = new CreatePostCmd(payload.url(), payload.title(), payload.content(), loginUserId);
        return postService.createPost(request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Post", security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<Void> deletePost(@PathVariable Long id) {
        SecurityUser loginUser = SecurityService.getCurrentUserOrThrow();
        PostDTO post = postService.getPostById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        this.checkPrivilege(post, loginUser);
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    private void checkPrivilege(PostDTO post, SecurityUser loginUser) {
        if (!(post.createdBy().id().equals(loginUser.getUserId()) || loginUser.isAdmin())) {
            throw new UnauthorisedAccessException("Unauthorised Access");
        }
    }

    record CreatePostRequestPayload(
            @NotBlank(message = "URL cannot be blank") String url,
            @NotBlank(message = "Title cannot be blank") String title,
            String content) {}
}

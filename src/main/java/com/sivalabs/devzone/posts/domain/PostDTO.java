package com.sivalabs.devzone.posts.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class PostDTO {
    private Long id;

    @NotBlank(message = "URL cannot be blank")
    private String url;

    private String title;

    private String content;

    private PostCreator createdBy;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCreator {
        private Long id;
        private String name;
    }

    public static PostDTO from(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setUrl(post.getUrl());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedBy(
                new PostCreator(post.getCreatedBy().getId(), post.getCreatedBy().getName()));
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }
}

package com.sivalabs.devzone.posts.domain;

import java.time.Instant;

public record PostDTO(
        Long id,
        String url,
        String title,
        String content,
        PostCreator createdBy,
        Instant createdAt,
        Instant updatedAt) {

    public PostDTO(
            Long id,
            String url,
            String title,
            String content,
            Long createdByUserId,
            String createdByUserName,
            Instant createdAt,
            Instant updatedAt) {
        this(id, url, title, content, new PostCreator(createdByUserId, createdByUserName), createdAt, updatedAt);
    }

    public record PostCreator(Long id, String name) {}
}

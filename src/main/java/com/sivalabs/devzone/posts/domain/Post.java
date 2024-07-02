package com.sivalabs.devzone.posts.domain;

import java.time.Instant;

public record Post(
        Long id, String url, String title, String content, Long createdBy, Instant createdAt, Instant updatedAt) {}

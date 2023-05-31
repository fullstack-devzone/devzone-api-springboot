package com.sivalabs.devzone.posts.models;

import jakarta.validation.constraints.NotBlank;

public record CreatePostRequest(
        @NotBlank(message = "URL cannot be blank") String title,
        @NotBlank(message = "URL cannot be blank") String url,
        String content,
        Long userId) {}

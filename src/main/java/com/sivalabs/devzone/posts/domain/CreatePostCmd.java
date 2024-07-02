package com.sivalabs.devzone.posts.domain;

public record CreatePostCmd(String url, String title, String content, Long userId) {}

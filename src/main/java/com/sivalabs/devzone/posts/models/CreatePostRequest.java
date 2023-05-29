package com.sivalabs.devzone.posts.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreatePostRequest {
    @NotBlank(message = "URL cannot be blank")
    private String title;

    @NotBlank(message = "URL cannot be blank")
    private String url;

    private String content;

    private Long userId;
}

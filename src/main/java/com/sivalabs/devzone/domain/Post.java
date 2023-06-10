package com.sivalabs.devzone.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String url;
    private String content;
    private User createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

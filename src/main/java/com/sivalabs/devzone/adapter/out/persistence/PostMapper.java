package com.sivalabs.devzone.adapter.out.persistence;

import com.sivalabs.devzone.domain.Post;

class PostMapper {
    public static Post fromEntity(PostEntity entity) {
        return new Post(
                entity.getId(),
                entity.getTitle(),
                entity.getUrl(),
                entity.getContent(),
                UserMapper.fromEntity(entity.getCreatedBy()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static PostEntity toEntity(Post post) {
        return new PostEntity(
                post.getId(),
                post.getTitle(),
                post.getUrl(),
                post.getContent(),
                UserMapper.toEntity(post.getCreatedBy()),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }
}

package com.sivalabs.devzone.posts.mappers;

import com.sivalabs.devzone.posts.entities.Post;
import com.sivalabs.devzone.posts.models.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {

    public PostDTO toDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setUrl(post.getUrl());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedUserId(post.getCreatedBy().getId());
        dto.setCreatedUserName(post.getCreatedBy().getName());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }
}

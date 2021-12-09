package com.sivalabs.devzone.domain.mappers;

import com.sivalabs.devzone.domain.entities.Link;
import com.sivalabs.devzone.domain.entities.Tag;
import com.sivalabs.devzone.domain.models.LinkDTO;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkMapper {

    public LinkDTO toDTO(Link link) {
        LinkDTO dto = new LinkDTO();
        dto.setId(link.getId());
        dto.setUrl(link.getUrl());
        dto.setTitle(link.getTitle());

        LinkDTO.CreatedUser createdBy = new LinkDTO.CreatedUser();
        createdBy.setId(link.getCreatedBy().getId());
        createdBy.setName(link.getCreatedBy().getName());
        dto.setCreatedBy(createdBy);

        dto.setCreatedAt(link.getCreatedAt());
        dto.setUpdatedAt(link.getUpdatedAt());

        if (link.getTags() != null) {
            dto.setTags(link.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
        }
        return dto;
    }
}

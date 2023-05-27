package com.sivalabs.devzone.posts.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostsDTO {
    private List<PostDTO> data;
    private long totalElements;
    private int pageNumber;
    private int totalPages;

    @JsonProperty("isFirst")
    private boolean isFirst;

    @JsonProperty("isLast")
    private boolean isLast;

    @JsonProperty("hasNext")
    private boolean hasNext;

    @JsonProperty("hasPrevious")
    private boolean hasPrevious;

    public PostsDTO(Page<PostDTO> postsPage) {
        this.setData(postsPage.getContent());
        this.setTotalElements(postsPage.getTotalElements());
        this.setPageNumber(postsPage.getNumber() + 1); // 1 - based page numbering
        this.setTotalPages(postsPage.getTotalPages());
        this.setFirst(postsPage.isFirst());
        this.setLast(postsPage.isLast());
        this.setHasNext(postsPage.hasNext());
        this.setHasPrevious(postsPage.hasPrevious());
    }
}

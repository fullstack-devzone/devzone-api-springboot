package com.sivalabs.devzone.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LinkDTO {
    private Long id;

    @NotBlank(message = "URL cannot be blank")
    private String url;

    private String title;

    @JsonProperty("createdBy")
    private CreatedUser createdBy;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    private List<String> tags = new ArrayList<>();

    @Setter
    @Getter
    public static class CreatedUser {
        private Long id;
        private String name;
    }
}

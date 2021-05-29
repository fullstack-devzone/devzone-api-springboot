package com.sivalabs.devzone.domain.models;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateLinkRequest {
    private Long id;

    @NotBlank(message = "URL cannot be blank")
    private String url;

    private String title;

    private List<String> tags = new ArrayList<>();
}

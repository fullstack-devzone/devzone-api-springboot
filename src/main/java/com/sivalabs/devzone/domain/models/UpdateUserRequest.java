package com.sivalabs.devzone.domain.models;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String bio;

    private String location;

    private String githubUsername;

    private String twitterUsername;

    private List<String> skills = new ArrayList<>();
}

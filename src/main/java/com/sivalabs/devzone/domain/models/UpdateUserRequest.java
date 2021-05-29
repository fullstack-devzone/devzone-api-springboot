package com.sivalabs.devzone.domain.models;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;
}

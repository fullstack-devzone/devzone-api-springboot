package com.sivalabs.devzone.domain.models;

import java.util.List;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserResponse {
    private Long id;

    private String name;

    private String email;

    private List<String> roles;
}

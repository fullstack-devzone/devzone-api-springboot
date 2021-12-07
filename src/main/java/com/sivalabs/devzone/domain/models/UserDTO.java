package com.sivalabs.devzone.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sivalabs.devzone.domain.entities.Role;
import com.sivalabs.devzone.domain.entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String imageUrl;

    private String bio;

    private String location;

    private String githubUsername;

    private String twitterUsername;

    private List<String> skills = new ArrayList<>();

    private List<String> roles;

    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setName(this.name);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setImageUrl(this.imageUrl);
        user.setBio(this.bio);
        user.setLocation(this.location);
        user.setGithubUsername(this.githubUsername);
        user.setTwitterUsername(this.twitterUsername);
        user.setSkills(this.skills);
        return user;
    }

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setImageUrl(user.getImageUrl());
        dto.setBio(user.getBio());
        dto.setLocation(user.getLocation());
        dto.setGithubUsername(user.getGithubUsername());
        dto.setTwitterUsername(user.getTwitterUsername());
        dto.setSkills(user.getSkills());
        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        }
        return dto;
    }
}

package com.sivalabs.devzone.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;

@Getter
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String name, String email, String password, Role role) {
        this(null, name, email, password, role, null, null);
    }

    public User(
            Long id,
            String name,
            String email,
            String password,
            Role role,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
        Objects.requireNonNull(role);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.updatedAt = updatedAt;
    }

    public boolean isAdmin() {
        return hasAnyRoles(Role.ROLE_ADMIN);
    }

    public boolean hasAnyRoles(Role... roles) {
        return Arrays.asList(roles).contains(this.getRole());
    }
}

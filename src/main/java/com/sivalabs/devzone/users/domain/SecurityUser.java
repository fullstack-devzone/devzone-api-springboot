package com.sivalabs.devzone.users.domain;

import java.util.Arrays;
import java.util.Set;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public class SecurityUser extends org.springframework.security.core.userdetails.User {
    private final Long userId;
    private final String name;
    private final String email;
    private final Role role;

    public SecurityUser(Long userId, String name, String email, String password, Role role) {
        super(email, password, Set.of(new SimpleGrantedAuthority(role.name())));
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public boolean isAdmin() {
        return hasAnyRole(Role.ROLE_ADMIN);
    }

    public boolean hasAnyRole(Role... roles) {
        return Arrays.asList(roles).contains(this.getRole());
    }
}

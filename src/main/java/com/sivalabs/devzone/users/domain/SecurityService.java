package com.sivalabs.devzone.users.domain;

import com.sivalabs.devzone.common.exceptions.UnauthorisedAccessException;
import com.sivalabs.devzone.security.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityService {
    static User loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUser securityUser) {
            return securityUser.getUser();
        }
        return null;
    }

    public static User getCurrentUserOrThrow() {
        User user = loginUser();
        if (user == null) {
            throw new UnauthorisedAccessException("User not logged in");
        }
        return user;
    }

    public static Long loginUserId() {
        User user = loginUser();
        return user != null ? user.getId() : null;
    }

    public static boolean isCurrentUserAdmin() {
        User user = loginUser();
        return user != null && user.isAdmin();
    }
}

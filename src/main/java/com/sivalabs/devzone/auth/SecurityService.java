package com.sivalabs.devzone.auth;

import com.sivalabs.devzone.common.exceptions.UnauthorisedAccessException;
import com.sivalabs.devzone.users.domain.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityService {
    static SecurityUser loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUser securityUser) {
            return securityUser;
        }
        return null;
    }

    public static SecurityUser getCurrentUserOrThrow() {
        SecurityUser user = loginUser();
        if (user == null) {
            throw new UnauthorisedAccessException("User not logged in");
        }
        return user;
    }

    public static Long loginUserId() {
        SecurityUser user = loginUser();
        return user != null ? user.getUserId() : null;
    }
}

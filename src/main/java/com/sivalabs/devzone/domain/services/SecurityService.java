package com.sivalabs.devzone.domain.services;

import static com.sivalabs.devzone.domain.utils.AppConstants.ROLE_ADMIN;
import static com.sivalabs.devzone.domain.utils.AppConstants.ROLE_MODERATOR;

import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.domain.entities.User;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SecurityService {

    public SecurityUser loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            return (SecurityUser) authentication.getPrincipal();
        }
        return null;
    }

    public boolean canCurrentUserEditLink(Long linkCreatedUserId) {
        final SecurityUser securityUser = loginUser();
        User loginUser = securityUser == null ? null : securityUser.getUser();
        if (loginUser == null) {
            return false;
        }
        final boolean isOwner = Objects.equals(linkCreatedUserId, loginUser.getId());
        return isOwner || isCurrentUserAdminOrModerator(loginUser);
    }

    public Long loginUserId() {
        final SecurityUser securityUser = loginUser();
        User loginUser = securityUser == null ? null : securityUser.getUser();
        if (loginUser != null) {
            return loginUser.getId();
        }
        return null;
    }

    public boolean isCurrentUserAdmin() {
        return isUserHasAnyRole(loginUser().getUser(), ROLE_ADMIN);
    }

    private boolean isCurrentUserAdminOrModerator(User loginUser) {
        return isUserHasAnyRole(loginUser, ROLE_ADMIN, ROLE_MODERATOR);
    }

    private boolean isUserHasAnyRole(User loginUser, String... roles) {
        List<String> roleList = Arrays.asList(roles);
        if (loginUser != null && loginUser.getRoles() != null) {
            return loginUser.getRoles().stream()
                    .anyMatch(role -> roleList.contains(role.getName()));
        }
        return false;
    }
}

package com.stormfarm.common.security.rbac;

import com.stormfarm.common.entity.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    
    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Long) {
            return (Long) auth.getPrincipal();
        }
        return null;
    }

    public Long id() {
        return getCurrentUserId();
    }

    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public void assertSelfOrAdmin(Long userId) {
        Long currentId = getCurrentUserId();
        if (!isAdmin() && (currentId == null || !currentId.equals(userId))) {
            throw new AccessDeniedException("Access denied");
        }
    }
}

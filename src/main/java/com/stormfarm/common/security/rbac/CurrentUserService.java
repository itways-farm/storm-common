package com.stormfarm.common.security.rbac;

import com.stormfarm.common.entity.enums.AccessScope;
import com.stormfarm.common.security.AuthenticatedUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Who is making this request, and what they are allowed to do.
 *
 * Everything here answers in terms of permissions and scope. There is
 * deliberately no {@code isAdmin()}: asking whether someone holds a particular
 * role defeats the point of roles being editable data, and it was how the
 * platform ended up with authorization rules that no administrator could change.
 */
@Service
public class CurrentUserService {

    // ── Identity ─────────────────────────────────────────────────────────────

    /** The signed-in user's id, or null when the request is anonymous. */
    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Long id) {
            return id;
        }
        return null;
    }

    /** Shorthand for {@link #getCurrentUserId()}. */
    public Long id() {
        return getCurrentUserId();
    }

    // ── What they may do ─────────────────────────────────────────────────────

    /** Every permission key the caller holds. */
    public Set<String> permissions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return Set.of();
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    /** True when the caller holds the given permission, e.g. {@code devices:manage}. */
    public boolean has(String permission) {
        return permissions().contains(permission);
    }

    /** Refuses the request unless the caller holds the permission. */
    public void require(String permission) {
        if (!has(permission)) {
            throw new AccessDeniedException("Missing permission: " + permission);
        }
    }

    // ── How far they can see ─────────────────────────────────────────────────

    /**
     * The caller's reach. Anonymous or malformed authentication yields the
     * narrowest scope, so a missing claim can never widen access.
     */
    public AccessScope scope() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getDetails() instanceof AuthenticatedUser details) {
            return details.scope();
        }
        return AccessScope.OWN;
    }

    /** True when the caller's reach extends at least as far as {@code required}. */
    public boolean reaches(AccessScope required) {
        return scope().reaches(required);
    }

    // ── Combined checks ──────────────────────────────────────────────────────

    /**
     * Allows the request when it targets the caller's own record, or when they
     * hold a permission that lets them act on other people's.
     */
    public void assertSelfOr(Long targetUserId, String permission) {
        Long currentId = getCurrentUserId();
        boolean isSelf = currentId != null && currentId.equals(targetUserId);
        if (!isSelf && !has(permission)) {
            throw new AccessDeniedException("Access denied");
        }
    }
}

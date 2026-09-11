package com.stormfarm.common.security;

import com.stormfarm.common.entity.Role;
import com.stormfarm.common.entity.User;
import com.stormfarm.common.entity.enums.AccessScope;

import java.util.List;
import java.util.Set;

/**
 * Everything an access token says about the person holding it.
 *
 * Grouping these into one value keeps the issuing call readable and means
 * adding a claim later is one field here rather than another parameter threaded
 * through every caller.
 *
 * {@code roles} is carried for display only — the console shows role names on a
 * profile. Authorization reads {@code permissions} and {@code scope}, never the
 * role names, so a renamed or newly invented role needs no code change.
 */
public record TokenPrincipal(
        Long userId,
        String email,
        String username,
        List<String> roles,
        Set<String> permissions,
        AccessScope scope
) {
    public TokenPrincipal {
        roles       = roles       == null ? List.of() : List.copyOf(roles);
        permissions = permissions == null ? Set.of()  : Set.copyOf(permissions);
        scope       = scope       == null ? AccessScope.OWN : scope;
    }

    /**
     * Reads a user's access off their roles.
     *
     * Permissions are the union of every role's permissions, and the scope is
     * the widest any role grants — holding two roles gives you the sum of what
     * they allow, never less than either alone. Both token issuers use this, so
     * signing in and accepting an invitation can never disagree about access.
     *
     * Must be called with the user's roles loaded; Role fetches its permissions
     * eagerly, so a User read through the repository already qualifies.
     */
    public static TokenPrincipal of(User user) {
        Set<Role> roles = user.getRoles() == null ? Set.of() : user.getRoles();

        List<String> roleNames = roles.stream()
                .map(Role::getName)
                .sorted()
                .toList();

        Set<String> permissions = roles.stream()
                .flatMap(role -> role.permissionKeys().stream())
                .collect(java.util.stream.Collectors.toSet());

        AccessScope scope = roles.stream()
                .map(Role::getScope)
                .reduce(AccessScope.OWN, AccessScope::widest);

        return new TokenPrincipal(
                user.getId(), user.getEmail(), user.getUsername(), roleNames, permissions, scope);
    }
}

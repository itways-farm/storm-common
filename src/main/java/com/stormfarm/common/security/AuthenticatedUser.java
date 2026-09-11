package com.stormfarm.common.security;

import com.stormfarm.common.entity.enums.AccessScope;

/**
 * The parts of the caller that authorization needs but Spring's authorities
 * cannot express.
 *
 * Permissions ride in the authentication's authorities, where {@code
 * @PreAuthorize} can see them. Scope has no equivalent there, so it travels as
 * the authentication's details and is read back through CurrentUserService.
 */
public record AuthenticatedUser(Long userId, AccessScope scope) {}

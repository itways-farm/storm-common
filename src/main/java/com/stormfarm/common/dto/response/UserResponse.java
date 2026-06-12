package com.stormfarm.common.dto.response;

import lombok.*;
import java.time.Instant;
import java.util.Set;
import com.stormfarm.common.entity.enums.UserStatus;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private UserStatus status;
    private boolean locked;
    private TeamResponse team;
    private Set<RoleResponse> roles;
    private int failedLoginCount;
    private Instant lockedUntil;
    private Instant createdAt;
    private Instant updatedAt;

    // Convenience getters for services that read individual fields
    public String getTeamName() { return team != null ? team.getName() : null; }
    public Long getTeamId()     { return team != null ? team.getId()   : null; }
    public Long getOrganizationId() {
        return (team != null && team.getOrganization() != null) ? team.getOrganization().getId() : null;
    }
}

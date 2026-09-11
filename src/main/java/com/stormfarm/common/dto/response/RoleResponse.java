package com.stormfarm.common.dto.response;

import com.stormfarm.common.entity.enums.AccessScope;
import lombok.*;

import java.util.Set;

/** A role as the console shows it: what it may do, how far it reaches, who holds it. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RoleResponse {
    private Long id;
    private String name;
    private String description;

    /** How far holders of this role can see. */
    private AccessScope scope;

    /** Ships with the platform, so it cannot be deleted; still fully editable. */
    private boolean systemRole;

    /** How many operatives currently hold this role. */
    private long userCount;

    private Set<PermissionResponse> permissions;
}

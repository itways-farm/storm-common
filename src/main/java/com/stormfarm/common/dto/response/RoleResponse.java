package com.stormfarm.common.dto.response;

import lombok.*;
import java.util.Set;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RoleResponse {
    private Long id;
    private String name;
    private Set<PermissionResponse> permissions;
}

package com.stormfarm.common.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PermissionResponse {
    private Long id;
    private String name;
}

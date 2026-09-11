package com.stormfarm.common.dto.response;

import lombok.*;

/** One capability, with the wording the console's permission grid displays. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PermissionResponse {
    private Long id;
    private String name;
    private String category;
    private String description;
}

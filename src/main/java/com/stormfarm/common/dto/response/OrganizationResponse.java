package com.stormfarm.common.dto.response;

import lombok.*;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrganizationResponse {
    private Long id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}

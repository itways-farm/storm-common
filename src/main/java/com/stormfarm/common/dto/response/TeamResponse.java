package com.stormfarm.common.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TeamResponse {
    private Long id;
    private String name;
    private String description;
    private OrganizationResponse organization;
    private java.time.Instant createdAt;
    private java.time.Instant updatedAt;
}

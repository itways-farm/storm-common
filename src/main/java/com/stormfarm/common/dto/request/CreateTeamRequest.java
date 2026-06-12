package com.stormfarm.common.dto.request;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateTeamRequest {
    private String name;
    private String description;
    private Long organizationId;
}

package com.stormfarm.common.dto.request;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateOrganizationRequest {
    private String name;
    private String domain;
}

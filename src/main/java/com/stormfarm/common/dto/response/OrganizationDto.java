package com.stormfarm.common.dto.response;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrganizationDto {
    private Long id;
    private String name;
    private String domain;
}

package com.stormfarm.common.dto.request;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class InviteUserRequest {
    private String email;
    private Long roleId;
    private Long teamId;
}

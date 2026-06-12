package com.stormfarm.common.dto.request;

import com.stormfarm.common.entity.enums.UserStatus;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateUserRequest {
    private String username;
    private String email;
    private UserStatus status;
    private Long teamId;
}

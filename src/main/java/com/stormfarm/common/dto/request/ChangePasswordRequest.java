package com.stormfarm.common.dto.request;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
}

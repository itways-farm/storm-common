package com.stormfarm.common.dto.request;

import com.stormfarm.common.entity.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateUserRequest {
    // Validated on both the admin (PATCH /{id}) and self-service (PATCH /me)
    // paths — without these a malformed email or blank username was accepted.
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Email
    private String email;

    // Admin-only fields: honoured by the admin update, ignored by self-service
    // profile updates (a user cannot change their own team or status).
    private UserStatus status;
    private Long teamId;
}

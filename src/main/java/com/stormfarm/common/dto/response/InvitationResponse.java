package com.stormfarm.common.dto.response;
import lombok.*;
import java.time.Instant;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class InvitationResponse {
    private Long id;
    private String email;
    private String invitedByUsername;
    private String roleName;
    private String teamName;
    private boolean accepted;
    private boolean expired;
    private Instant expiresAt;
    private Instant createdAt;
}

package com.stormfarm.common.dto.request;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AcceptInvitationRequest {
    private String token;
    private String username;
    private String password;
}

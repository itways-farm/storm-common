package com.stormfarm.common.dto.response;
import lombok.*;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}

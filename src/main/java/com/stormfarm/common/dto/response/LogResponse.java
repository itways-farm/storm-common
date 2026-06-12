package com.stormfarm.common.dto.response;
import lombok.*;
import java.time.Instant;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LogResponse {
    private Long id;
    private String level;
    private String message;
    private Instant timestamp;
}

package com.stormfarm.common.dto.response;

import lombok.*;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LogDto {
    private Long id;
    private Instant timestamp;
    private String level;
    private String message;
    private String tag;
    private String threadName;
}

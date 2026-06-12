package com.stormfarm.common.dto.response;
import lombok.*;
import java.time.Instant;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TestJobExecutionDto {
    private Long id;
    private Long testJobId;
    private String status;
    private Instant executedAt;
    private Instant completedAt;
    private String stepResults;
    private String errorMessage;
    private Long durationMs;
}

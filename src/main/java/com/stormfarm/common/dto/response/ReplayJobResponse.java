package com.stormfarm.common.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReplayJobResponse {
    private Long executionId;
    private Long deviceId;
    private String status;
    private String message;
}

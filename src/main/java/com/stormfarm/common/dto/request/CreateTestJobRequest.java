package com.stormfarm.common.dto.request;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateTestJobRequest {
    private Long deviceId;
    private String scriptPath;
    private String scriptContent;
}

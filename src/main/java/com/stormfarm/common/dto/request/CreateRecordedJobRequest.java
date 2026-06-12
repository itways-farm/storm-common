package com.stormfarm.common.dto.request;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateRecordedJobRequest {
    private Long deviceId;
    private String name;
    private String scriptContent;
}

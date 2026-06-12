package com.stormfarm.common.dto.response;
import lombok.*;
import java.time.Instant;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TestJobDto {
    private Long id;
    private Long deviceId;
    private String deviceName;
    private String deviceModel;
    private String deviceStatus;
    private String status;
    private String result;
    private String name;
    private String scriptType;
    private Instant createdAt;
    private Long executionCount;
}

package com.stormfarm.common.dto.response;

import lombok.*;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DeviceUtilizationResponse {
    private Long deviceId;
    private String deviceName;
    private String platform;
    private LocalDate day;
    private long totalDuration;
    private double passRate;
    private long sessionCount;
}

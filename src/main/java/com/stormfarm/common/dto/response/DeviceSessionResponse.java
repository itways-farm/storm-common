package com.stormfarm.common.dto.response;

import com.stormfarm.common.entity.enums.SessionStatus;
import lombok.*;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DeviceSessionResponse {
    private Long id;
    private Long deviceId;
    private String deviceName;
    private String deviceModel;
    private String deviceBrand;
    private String devicePlatform;
    private String deviceOsVersion;
    private String deviceConnectionType;
    private String deviceStatus;
    private Long userId;
    private String username;
    private SessionStatus status;
    private Instant startTime;
    private Instant endTime;
}

package com.stormfarm.common.dto.response;

import com.stormfarm.common.entity.enums.DevicePlatform;
import com.stormfarm.common.entity.enums.DeviceStatus;
import lombok.*;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DeviceDto {
    private Long id;
    private String serial;
    private String stfSerial;
    private String model;
    private String displayName;
    private String brand;
    private String imageSlug;
    private DevicePlatform platform;
    private DeviceStatus status;
    private Boolean presence;
    private Instant lastSeen;
    private String ipAddress;
    private String osVersion;
    private String connectionType;
    private Boolean stfPresent;
    private Boolean screenStreaming;
    private Boolean inputControl;
    private Boolean installApp;

    public String getName() { return displayName; }
    public void setName(String name) { this.displayName = name; }
}

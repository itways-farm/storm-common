package com.stormfarm.common.dto.response;

import com.stormfarm.common.entity.enums.DevicePlatform;
import com.stormfarm.common.entity.enums.DeviceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DeviceDto {
    private Long id;
    private String serial;
    private String stfSerial;
    private String model;
    // Required when creating/updating a device (validated via @Valid on the
    // controller); JSON field "name" maps here through getName/setName. Without
    // these, a missing name/platform NPE'd in the mapper and surfaced as a 500.
    @NotBlank
    private String displayName;
    private String brand;
    private String imageSlug;
    @NotNull
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
    private Long lockedByTeamId;
    private String lockedByTeamName;
    private Boolean locked;
    private Boolean lockedToMyTeam;

    public String getName() { return displayName; }
    public void setName(String name) { this.displayName = name; }
}

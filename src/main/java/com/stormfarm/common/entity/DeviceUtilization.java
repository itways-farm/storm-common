package com.stormfarm.common.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "mv_device_utilization")
@Builder @NoArgsConstructor @AllArgsConstructor
public class DeviceUtilization {

    @EmbeddedId
    private DeviceUtilizationId id;

    @Column(name = "device_name")
    private String deviceName;

    private String platform;

    @Column(name = "session_count")
    private long sessionCount;

    @Column(name = "total_hours")
    private BigDecimal totalHours;

    public DeviceUtilizationId getId() { return id; }
    public void setId(DeviceUtilizationId id) { this.id = id; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public long getSessionCount() { return sessionCount; }
    public void setSessionCount(long sessionCount) { this.sessionCount = sessionCount; }
    public BigDecimal getTotalHours() { return totalHours; }
    public void setTotalHours(BigDecimal totalHours) { this.totalHours = totalHours; }

    public Long getDeviceId() { return id != null ? id.getDeviceId() : null; }
    public LocalDate getDay() { return id != null ? id.getDay() : null; }

    @Embeddable
    @Builder @NoArgsConstructor @AllArgsConstructor
    @Data
    public static class DeviceUtilizationId implements Serializable {
        @Column(name = "device_id")
        private Long deviceId;
        private LocalDate day;
    }
}

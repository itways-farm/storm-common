package com.stormfarm.common.dto.response;

import lombok.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardResponse {
    private long totalJobs;
    private long runningJobs;
    private long passedJobs;
    private long failedJobs;
    private long pendingJobs;
    private long cancelledJobs;
    private double fleetReliability;
    private long activeSessions;
    private long totalSessions;
    private long totalDevices;
    private long availableDevices;
    private long busyDevices;
    private long offlineDevices;

    private List<JobStatusPoint> jobStatusBreakdown;
    private List<DeviceStatusPoint> deviceStatusBreakdown;
    private List<DailyVolumePoint> dailyVolume;
    private List<TopDevicePoint> topDevices;

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class JobStatusPoint {
        private String status;
        private long count;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class DeviceStatusPoint {
        private String status;
        private long count;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class DailyVolumePoint {
        private String day;
        private long total;
        private long passed;
        private long failed;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class TopDevicePoint {
        private String deviceName;
        private String platform;
        private long sessions;
        private long durationMinutes;
        private double passRate;
    }
}

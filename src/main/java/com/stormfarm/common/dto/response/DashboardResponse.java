package com.stormfarm.common.dto.response;

import lombok.*;
import java.util.List;

/**
 * The dashboard summary.
 *
 * This used to be built almost entirely from automated test jobs — six of its
 * tiles, the status chart, the daily volume chart and the top-device pass rate
 * were all job counts. Automated jobs were removed from the platform, so the
 * dashboard is now built from the three things the product actually records:
 * device sessions, the device fleet itself, and defects.
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardResponse {

    // ── Sessions ─────────────────────────────────────────────────────────────
    private long totalSessions;
    private long activeSessions;
    private long totalSessionMinutes;

    // ── Fleet ────────────────────────────────────────────────────────────────
    private long totalDevices;
    private long availableDevices;
    private long busyDevices;
    private long offlineDevices;
    /**
     * Share of the fleet that is reachable (anything not offline), as a
     * percentage. Replaces the old fleetReliability, which was a job pass rate
     * and so had nothing left behind it.
     */
    private double fleetAvailability;

    // ── Defects ──────────────────────────────────────────────────────────────
    private long totalDefects;
    private long openDefects;
    private long resolvedDefects;
    /** Share of defects that have been resolved or closed, as a percentage. */
    private double defectResolutionRate;

    // ── Charts ───────────────────────────────────────────────────────────────
    private List<DeviceStatusPoint> deviceStatusBreakdown;
    private List<DefectSeverityPoint> defectSeverityBreakdown;
    private List<DailyActivityPoint> dailyActivity;
    private List<TopDevicePoint> topDevices;

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class DeviceStatusPoint {
        private String status;
        private long count;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class DefectSeverityPoint {
        private String severity;
        private long count;
    }

    /** One day's activity: sessions started, and defects raised and closed. */
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class DailyActivityPoint {
        private String day;
        private long sessions;
        private long defectsOpened;
        private long defectsResolved;
    }

    /** A device's share of the work: how much it was used, and what was found on it. */
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class TopDevicePoint {
        private String deviceName;
        private String platform;
        private long sessions;
        private long durationMinutes;
        private long defects;
    }
}

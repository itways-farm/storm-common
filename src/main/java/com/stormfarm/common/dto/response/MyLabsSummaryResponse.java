package com.stormfarm.common.dto.response;

import lombok.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MyLabsSummaryResponse {
    private long totalJobs;
    private long passedJobs;
    private long failedJobs;
    private long runningJobs;
    private long pendingJobs;
    private long cancelledJobs;
    private double passRate;
    private long totalSessions;
    private long activeSessions;
    private long totalSessionMinutes;
    private List<DayPoint> dailyBreakdown;

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class DayPoint {
        private String day;
        private long total;
        private long passed;
        private long failed;
        private long sessions;
    }
}

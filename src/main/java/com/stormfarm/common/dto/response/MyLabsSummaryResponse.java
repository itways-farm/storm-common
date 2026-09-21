package com.stormfarm.common.dto.response;

import lombok.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MyLabsSummaryResponse {
    private long totalSessions;
    private long activeSessions;
    private long totalSessionMinutes;
    private List<DayPoint> dailyBreakdown;

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class DayPoint {
        private String day;
        private long sessions;
    }
}

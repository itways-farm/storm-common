package com.stormfarm.common.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Table(name = "mv_user_test_summary")
@Builder @NoArgsConstructor @AllArgsConstructor
public class UserTestSummary {
    @Id
    @Column(name = "user_id")
    private Long userId;

    private String username;

    @Column(name = "total_jobs")
    private long totalJobs;

    private long passed;
    private long failed;

    @Column(name = "last_job_at")
    private Instant lastJobAt;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public long getTotalJobs() { return totalJobs; }
    public void setTotalJobs(long totalJobs) { this.totalJobs = totalJobs; }
    public long getPassed() { return passed; }
    public void setPassed(long passed) { this.passed = passed; }
    public long getPassedJobs() { return passed; }
    public void setPassedJobs(long passed) { this.passed = passed; }
    public long getFailed() { return failed; }
    public void setFailed(long failed) { this.failed = failed; }
    public long getFailedJobs() { return failed; }
    public void setFailedJobs(long failed) { this.failed = failed; }
    public Instant getLastJobAt() { return lastJobAt; }
    public void setLastJobAt(Instant lastJobAt) { this.lastJobAt = lastJobAt; }
}

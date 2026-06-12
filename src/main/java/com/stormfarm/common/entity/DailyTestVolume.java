package com.stormfarm.common.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "daily_test_volumes")
@Builder @NoArgsConstructor @AllArgsConstructor
public class DailyTestVolume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate day;
    private long totalJobs;
    private long passedJobs;
    private long failedJobs;
    private long errorJobs;
    private Integer volume;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDay() { return day; }
    public void setDay(LocalDate day) { this.day = day; }
    public long getTotalJobs() { return totalJobs; }
    public void setTotalJobs(long totalJobs) { this.totalJobs = totalJobs; }
    public long getPassedJobs() { return passedJobs; }
    public void setPassedJobs(long passedJobs) { this.passedJobs = passedJobs; }
    public long getFailedJobs() { return failedJobs; }
    public void setFailedJobs(long failedJobs) { this.failedJobs = failedJobs; }
    public long getErrorJobs() { return errorJobs; }
    public void setErrorJobs(long errorJobs) { this.errorJobs = errorJobs; }
    public Integer getVolume() { return volume; }
    public void setVolume(Integer volume) { this.volume = volume; }
}

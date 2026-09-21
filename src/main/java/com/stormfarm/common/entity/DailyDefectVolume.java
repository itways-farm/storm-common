package com.stormfarm.common.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

/**
 * Read-only projection over {@code mv_daily_defect_volume}: how many defects
 * were raised and closed on each day.
 *
 * Replaces the daily *test job* volume this view used to carry. Automated jobs
 * were removed from the platform, so the chart is now driven by defect activity,
 * which is data the product actually produces.
 */
@Entity
@Table(name = "mv_daily_defect_volume")
@Builder @NoArgsConstructor @AllArgsConstructor
public class DailyDefectVolume {
    @Id
    private LocalDate day;

    @Column(name = "total_defects")
    private long totalDefects;

    @Column(name = "opened_defects")
    private long openedDefects;

    @Column(name = "resolved_defects")
    private long resolvedDefects;

    public LocalDate getDay() { return day; }
    public void setDay(LocalDate day) { this.day = day; }
    public long getTotalDefects() { return totalDefects; }
    public void setTotalDefects(long totalDefects) { this.totalDefects = totalDefects; }
    public long getOpenedDefects() { return openedDefects; }
    public void setOpenedDefects(long openedDefects) { this.openedDefects = openedDefects; }
    public long getResolvedDefects() { return resolvedDefects; }
    public void setResolvedDefects(long resolvedDefects) { this.resolvedDefects = resolvedDefects; }
}

package com.stormfarm.common.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

/**
 * Read-only projection over {@code mv_user_defect_summary}: what each operative
 * has reported.
 *
 * Replaces the per-user *test job* summary. With automated jobs removed, the
 * meaningful per-person measure is the defects they raised and how many of them
 * are still open.
 */
@Entity
@Table(name = "mv_user_defect_summary")
@Builder @NoArgsConstructor @AllArgsConstructor
public class UserDefectSummary {
    @Id
    @Column(name = "user_id")
    private Long userId;

    private String username;

    /**
     * Tenant scoping. V61 added these to the user summary because the endpoint
     * had been returning every organization's users to anyone holding
     * analytics:view; the replacement view must keep them or that leak returns.
     */
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "total_defects")
    private long totalDefects;

    @Column(name = "open_defects")
    private long openDefects;

    @Column(name = "resolved_defects")
    private long resolvedDefects;

    @Column(name = "last_defect_at")
    private Instant lastDefectAt;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public long getTotalDefects() { return totalDefects; }
    public void setTotalDefects(long totalDefects) { this.totalDefects = totalDefects; }
    public long getOpenDefects() { return openDefects; }
    public void setOpenDefects(long openDefects) { this.openDefects = openDefects; }
    public long getResolvedDefects() { return resolvedDefects; }
    public void setResolvedDefects(long resolvedDefects) { this.resolvedDefects = resolvedDefects; }
    public Instant getLastDefectAt() { return lastDefectAt; }
    public void setLastDefectAt(Instant lastDefectAt) { this.lastDefectAt = lastDefectAt; }
}

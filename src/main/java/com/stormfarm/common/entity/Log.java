package com.stormfarm.common.entity;
import jakarta.persistence.*;
import java.time.Instant;

import lombok.*;

@Entity @Table(name = "logs")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Log {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String level;
    private String message;
    @Builder.Default
    private Instant createdAt = Instant.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_job_id")
    private TestJob testJob;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public TestJob getTestJob() { return testJob; }
    public void setTestJob(TestJob testJob) { this.testJob = testJob; }
}

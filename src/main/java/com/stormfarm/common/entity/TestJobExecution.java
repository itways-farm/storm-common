package com.stormfarm.common.entity;

import com.stormfarm.common.entity.enums.TestJobStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "test_job_executions")
public class TestJobExecution {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "test_job_id") private TestJob testJob;
    @Enumerated(EnumType.STRING) private TestJobStatus status;
    private Instant executedAt;
    private Instant completedAt;
    private String stepResults;
    private String errorMessage;

    public TestJobExecution() {}

    @jakarta.persistence.PrePersist
    void prePersist() {
        if (executedAt == null) executedAt = java.time.Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TestJob getTestJob() { return testJob; }
    public void setTestJob(TestJob testJob) { this.testJob = testJob; }
    public TestJobStatus getStatus() { return status; }
    public void setStatus(TestJobStatus status) { this.status = status; }
    public Instant getExecutedAt() { return executedAt; }
    public void setExecutedAt(Instant executedAt) { this.executedAt = executedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
    public String getStepResults() { return stepResults; }
    public void setStepResults(String stepResults) { this.stepResults = stepResults; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}

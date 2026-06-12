package com.stormfarm.common.entity;
import jakarta.persistence.*;
import com.stormfarm.common.entity.enums.TestResultStatus;
import lombok.*;

@Entity @Table(name = "test_results")
@Builder @NoArgsConstructor @AllArgsConstructor
public class TestResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "test_job_id") 
    private TestJob testJob;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "execution_id") 
    private TestJobExecution execution;
    
    @Enumerated(EnumType.STRING)
    private TestResultStatus result;
    
    @Column(columnDefinition = "TEXT")
    private String logs;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TestJob getTestJob() { return testJob; }
    public void setTestJob(TestJob testJob) { this.testJob = testJob; }
    public TestJobExecution getExecution() { return execution; }
    public void setExecution(TestJobExecution execution) { this.execution = execution; }
    public TestResultStatus getResult() { return result; }
    public void setResult(TestResultStatus result) { this.result = result; }
    public String getLogs() { return logs; }
    public void setLogs(String logs) { this.logs = logs; }
}

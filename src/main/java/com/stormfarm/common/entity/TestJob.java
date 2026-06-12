package com.stormfarm.common.entity;

import com.stormfarm.common.entity.enums.TestJobStatus;
import com.stormfarm.common.entity.enums.TestJobScriptType;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "test_jobs")
public class TestJob {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    private String scriptPath;
    @Column(columnDefinition = "TEXT") private String scriptContent;
    @Enumerated(EnumType.STRING) private TestJobScriptType scriptType;
    @Enumerated(EnumType.STRING) private TestJobStatus status;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "device_id") private Device device;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") private User user;
    private Instant createdAt;

    // Support builder-like pattern for legacy code
    public static TestJobBuilder builder() { return new TestJobBuilder(); }
    public static class TestJobBuilder {
        private TestJob j = new TestJob();
        public TestJobBuilder name(String n) { j.name = n; return this; }
        public TestJobBuilder device(Device d) { j.device = d; return this; }
        public TestJobBuilder user(User u) { j.user = u; return this; }
        public TestJobBuilder scriptPath(String p) { j.scriptPath = p; return this; }
        public TestJobBuilder scriptType(TestJobScriptType t) { j.scriptType = t; return this; }
        public TestJobBuilder scriptContent(String c) { j.scriptContent = c; return this; }
        public TestJobBuilder status(TestJobStatus s) { j.status = s; return this; }
        public TestJob build() { j.createdAt = Instant.now(); return j; }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getScriptPath() { return scriptPath; }
    public void setScriptPath(String scriptPath) { this.scriptPath = scriptPath; }
    public String getScriptContent() { return scriptContent; }
    public void setScriptContent(String scriptContent) { this.scriptContent = scriptContent; }
    public TestJobScriptType getScriptType() { return scriptType; }
    public void setScriptType(TestJobScriptType scriptType) { this.scriptType = scriptType; }
    public TestJobStatus getStatus() { return status; }
    public void setStatus(TestJobStatus status) { this.status = status; }
    public Device getDevice() { return device; }
    public void setDevice(Device device) { this.device = device; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    @OneToMany(mappedBy = "testJob", fetch = FetchType.LAZY)
    private java.util.List<TestResult> testResults = new java.util.ArrayList<>();

    public java.util.List<TestResult> getTestResults() { return testResults; }
    public void setTestResults(java.util.List<TestResult> testResults) { this.testResults = testResults; }

    public TestResult getTestResult() {
        if (testResults != null && !testResults.isEmpty()) {
            return testResults.get(testResults.size() - 1);
        }
        return null;
    }
}

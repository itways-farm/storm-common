package com.stormfarm.common.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name = "login_attempts")
public class LoginAttempt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String email;
    private String ipAddress;
    private Instant attemptedAt;
    private boolean success;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public Instant getAttemptedAt() { return attemptedAt; }
    public void setAttemptedAt(Instant attemptedAt) { this.attemptedAt = attemptedAt; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}

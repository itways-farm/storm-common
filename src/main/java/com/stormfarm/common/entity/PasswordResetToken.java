package com.stormfarm.common.entity;
import jakarta.persistence.*;
@Entity @Table(name = "password_reset_tokens")
public class PasswordResetToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String token;
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") private User user;
    private boolean used = false;
    private java.time.Instant expiresAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
    public java.time.Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(java.time.Instant expiresAt) { this.expiresAt = expiresAt; }
}

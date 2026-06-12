package com.stormfarm.common.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Table(name = "revoked_tokens")
@Builder @NoArgsConstructor @AllArgsConstructor
public class RevokedToken {
    @Id
    @Column(name = "jti")
    private String jti;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "revoked_at", nullable = false)
    private Instant revokedAt = Instant.now();

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    public String getJti() { return jti; }
    public void setJti(String jti) { this.jti = jti; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Instant getRevokedAt() { return revokedAt; }
    public void setRevokedAt(Instant revokedAt) { this.revokedAt = revokedAt; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}

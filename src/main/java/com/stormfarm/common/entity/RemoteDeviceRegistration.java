package com.stormfarm.common.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "remote_device_registrations")
@Builder @NoArgsConstructor @AllArgsConstructor
public class RemoteDeviceRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** DB column name is 'token' (not 'registration_token') */
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "device_serial")
    private String deviceSerial;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "virtual_port")
    private Integer virtualPort;

    @Column(length = 50)
    private String status;

    @Builder.Default
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    // ── Accessors ────────────────────────────────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    /** Primary accessor used by RemoteBridgeService */
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    /** Alias kept for backward-compat with any call-sites using getRegistrationToken() */
    public String getRegistrationToken() { return token; }
    public void setRegistrationToken(String t) { this.token = t; }

    public String getDeviceSerial() { return deviceSerial; }
    public void setDeviceSerial(String deviceSerial) { this.deviceSerial = deviceSerial; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public Integer getVirtualPort() { return virtualPort; }
    public void setVirtualPort(Integer virtualPort) { this.virtualPort = virtualPort; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}

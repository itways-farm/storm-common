package com.stormfarm.common.entity;

import com.stormfarm.common.entity.enums.DeviceStatus;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Table(name = "devices")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String serial;

    @Column(name = "stf_serial", unique = true, length = 100)
    private String stfSerial;

    @Column(length = 100)
    private String model;

    @Column(length = 50)
    private String platform; // ANDROID, IOS

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DeviceStatus status = DeviceStatus.AVAILABLE;

    @Column(nullable = false)
    private Boolean presence = false;

    @Column(name = "last_seen")
    private Instant lastSeen = Instant.now();

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "os_version", length = 20)
    private String osVersion;

    @Column(name = "connection_type")
    private String connectionType;

    @Column(name = "stf_present")
    private Boolean stfPresent = false;

    @Column(name = "screen_streaming")
    private Boolean screenStreaming = true;

    @Column(name = "input_control")
    private Boolean inputControl = true;

    @Column(name = "install_app")
    private Boolean installApp = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locked_by_team_id")
    private Team lockedByTeam;

    private String name;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSerial() { return serial; }
    public void setSerial(String serial) { this.serial = serial; }
    public String getStfSerial() { return stfSerial; }
    public void setStfSerial(String stfSerial) { this.stfSerial = stfSerial; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public DeviceStatus getStatus() { return status; }
    public void setStatus(DeviceStatus status) { this.status = status; }
    
    public String getState() { return status != null ? status.name() : null; }
    public void setState(String state) { 
        try { this.status = DeviceStatus.valueOf(state); }
        catch (Exception e) { this.status = DeviceStatus.AVAILABLE; }
    }

    public Boolean getPresence() { return presence; }
    public void setPresence(Boolean presence) { this.presence = presence; }
    public Instant getLastSeen() { return lastSeen; }
    public void setLastSeen(Instant lastSeen) { this.lastSeen = lastSeen; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getOsVersion() { return osVersion; }
    public void setOsVersion(String osVersion) { this.osVersion = osVersion; }
    public String getConnectionType() { return connectionType; }
    public void setConnectionType(String connectionType) { this.connectionType = connectionType; }
    public Boolean getStfPresent() { return stfPresent; }
    public void setStfPresent(Boolean stfPresent) { this.stfPresent = stfPresent; }
    public Boolean getScreenStreaming() { return screenStreaming; }
    public void setScreenStreaming(Boolean screenStreaming) { this.screenStreaming = screenStreaming; }
    public Boolean getInputControl() { return inputControl; }
    public void setInputControl(Boolean inputControl) { this.inputControl = inputControl; }
    public Boolean getInstallApp() { return installApp; }
    public void setInstallApp(Boolean installApp) { this.installApp = installApp; }
    public Team getLockedByTeam() { return lockedByTeam; }
    public void setLockedByTeam(Team lockedByTeam) { this.lockedByTeam = lockedByTeam; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

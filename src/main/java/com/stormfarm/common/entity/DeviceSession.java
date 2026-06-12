package com.stormfarm.common.entity;
import jakarta.persistence.*;
import lombok.*;
import com.stormfarm.common.entity.enums.SessionStatus;
import java.time.Instant;
@Entity @Table(name = "device_sessions")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DeviceSession {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") private User user;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "device_id") private Device device;
    private Instant startTime;
    private Instant endTime;
    @Enumerated(EnumType.STRING) private SessionStatus status;
}

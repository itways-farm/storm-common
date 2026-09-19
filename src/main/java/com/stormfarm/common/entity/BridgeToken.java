package com.stormfarm.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * A managed credential a device bridge presents to connect. Each token is an
 * independent, revocable record so a single compromised or retired machine can
 * be cut off without disrupting the others, and so live status can be attributed
 * to a specific token. Shared entity: validated by storm-device-detector (the
 * /ws/bridge handshake) and storm-stream (the media upstream sockets), and
 * managed (CRUD) by storm-core.
 */
@Entity
@Table(name = "bridge_tokens")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class BridgeToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Human label so an admin knows which machine/rig a token belongs to. */
    @Column(nullable = false, length = 120)
    private String label;

    /** The secret the bridge presents; unique so it can be looked up directly. */
    @Column(nullable = false, unique = true, length = 255)
    private String token;

    /** Optional platform hint: ANDROID, IOS or ANY. */
    @Column(length = 20)
    private String platform;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /** Refreshed by the detector whenever a bridge using this token is seen. */
    @Column(name = "last_seen_at")
    private Instant lastSeenAt;
}

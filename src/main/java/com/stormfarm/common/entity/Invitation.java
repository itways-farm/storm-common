package com.stormfarm.common.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
@Entity @Table(name = "invitations")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Invitation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String email;
    private String token;
    private boolean accepted;
    private Instant expiresAt;
    private Instant createdAt;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "invited_by") private User invitedBy;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "role_id") private Role role;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "team_id") private Team team;
    @PrePersist void prePersist() { if (createdAt == null) createdAt = Instant.now(); }
    public boolean isExpired() { return expiresAt != null && expiresAt.isBefore(Instant.now()); }
}

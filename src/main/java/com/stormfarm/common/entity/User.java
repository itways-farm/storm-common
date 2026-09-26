package com.stormfarm.common.entity;

import com.stormfarm.common.entity.enums.UserStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Optimistic lock. Two services write this row — storm-core owns the
     * profile, roles and team; storm-authentication owns failedLoginCount and
     * lockedUntil — and a JPA save() writes every mapped column, not the two
     * that changed. Without this, a service that loaded the row and saved it
     * after another changed a different field silently reverts that change,
     * last writer wins, with nothing to notice.
     *
     * Not theoretical: the same shape already happened on devices, where the
     * detector's re-announce clobbered a name set in the console (P26-1), and
     * the fix there was a side table holding the rename separately — a
     * workaround for the shared write rather than a removal of it.
     *
     * A conflicting save now throws OptimisticLockException instead of losing
     * the write. Callers on a contended path must retry rather than let that
     * reach the user: the failed-login counter is the one to watch.
     */
    @Version
    @Column(nullable = false)
    private Long version;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "locked_until")
    private Instant lockedUntil;

    @Column(name = "failed_login_count")
    private int failedLoginCount = 0;

    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    private Instant updatedAt = Instant.now();

    public boolean isLocked() {
        return lockedUntil != null && lockedUntil.isAfter(Instant.now());
    }
}

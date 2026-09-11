package com.stormfarm.common.entity.enums;

/**
 * Lifecycle state of an account, and the single source of truth for it.
 *
 * These three values are exactly what the {@code users_status_check} database
 * constraint accepts and exactly what the web console offers, so a status set
 * anywhere is a status every layer understands.
 *
 * Being locked out after too many failed logins is deliberately NOT a value
 * here: it is a separate, temporary axis carried by {@code User.lockedUntil}
 * and reported by {@code User.isLocked()}. A user can be ACTIVE and locked at
 * the same time, and unlocking must not have to guess what status to restore.
 */
public enum UserStatus {
    /** Normal account. The only status that may log in. */
    ACTIVE,
    /** Switched off, e.g. someone who left. Reversible, keeps all history. */
    INACTIVE,
    /** Barred pending review. Reversible, keeps all history. */
    SUSPENDED
}

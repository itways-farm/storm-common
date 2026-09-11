package com.stormfarm.common.entity.enums;

/**
 * How far a role can see — whose work it may read and act on.
 *
 * This is the second half of a role, alongside its permissions: the permissions
 * say what someone may do, the scope says who they may do it to. Before this
 * existed each feature invented its own answer, so a tester saw every session in
 * the organization but only their own test jobs. One setting now decides it
 * everywhere.
 *
 * The order of the constants is the widening order, and {@link #widest} relies
 * on it: a user holding several roles gets the widest scope among them.
 */
public enum AccessScope {

    /** Only records the user owns. */
    OWN,

    /** Everything owned by members of the user's team. */
    TEAM,

    /** Everything owned by members of the user's organization. */
    ORGANIZATION,

    /** The whole installation, unfiltered. */
    ALL;

    /** True when this scope reaches at least as far as {@code other}. */
    public boolean reaches(AccessScope other) {
        return this.ordinal() >= other.ordinal();
    }

    /** The widest of two scopes; null is treated as the narrowest. */
    public static AccessScope widest(AccessScope a, AccessScope b) {
        if (a == null) return b == null ? OWN : b;
        if (b == null) return a;
        return a.ordinal() >= b.ordinal() ? a : b;
    }

    /** Parses a stored or claimed value, falling back to the narrowest scope. */
    public static AccessScope parse(String raw) {
        if (raw == null || raw.isBlank()) return OWN;
        try {
            return valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return OWN;
        }
    }
}

package com.stormfarm.common.repository;

/**
 * Makes user-typed search text safe to put inside a SQL LIKE pattern, so that
 * '%' and '_' are matched literally instead of as wildcards (BUG-0093).
 *
 * <p>The rule is the one every service uses: '!' is the escape character, and
 * '!', '%' and '_' are each prefixed with it. A query that takes the escaped
 * text must declare the same character, e.g.
 * {@code LIKE LOWER(CONCAT('%', :search, '%')) ESCAPE '!'}.
 */
public final class LikePattern {

    /** The escape character; queries declare it with {@code ESCAPE '!'}. */
    public static final char ESCAPE = '!';

    private LikePattern() {}

    /** Escapes LIKE wildcards in {@code raw}; null stays null so "no filter" checks still work. */
    public static String escape(String raw) {
        if (raw == null) return null;
        return raw.replace("!", "!!").replace("%", "!%").replace("_", "!_");
    }
}

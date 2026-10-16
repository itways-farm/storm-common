package com.stormfarm.common.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * The stored form of a credential the platform holds on somebody else's
 * behalf (an SMTP password, a Jira token, the LDAP bind password, ...):
 *
 * <pre>
 *   enc:v1:Base64( IV[12] || AES-256-GCM ciphertext || tag[16] )
 * </pre>
 *
 * storm-core writes these values and storm-authentication reads them from the
 * same table, so the format lives here, once. Before, each service carried its
 * own copy of the constants, and a change on one side (a v2 scheme, a key
 * rotation) would have broken LDAP sign-in on the other (BUG-0201).
 *
 * Only the format is shared. Where the key comes from is each service's own
 * business: storm-core generates and owns the key file, storm-authentication
 * only ever reads it. This class holds no key and no state, and is not a
 * Spring bean.
 *
 * The format is fixed: values already in databases must keep decrypting.
 * A new scheme gets a new prefix ({@code enc:v2:}) and this class keeps
 * reading v1.
 */
public final class StoredSecretCodec {

    /** Marks a stored value as encrypted. Version included so the scheme can change. */
    public static final String PREFIX = "enc:v1:";

    /** Length of the raw AES key: 256 bits. The key is configured and stored as Base64 of these bytes. */
    public static final int KEY_BYTES = 32;

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_BYTES = 12;
    private static final int TAG_BITS = 128;

    private static final SecureRandom RANDOM = new SecureRandom();

    private StoredSecretCodec() {
    }

    /** True when the value carries the marker of an encrypted value. */
    public static boolean isEncrypted(String value) {
        return value != null && value.startsWith(PREFIX);
    }

    /**
     * True when the setting with this key holds a credential rather than a
     * configuration value: it is stored encrypted, and it is the only kind of
     * setting that is decrypted on read.
     */
    public static boolean isSecretKey(String settingKey) {
        return settingKey != null
                && (settingKey.endsWith("password") || settingKey.endsWith("token") || settingKey.endsWith("secret"));
    }

    /**
     * The AES key for a Base64-encoded key (from {@code CASTFARM_SECRETS_KEY}
     * or the key file). Surrounding whitespace, such as a file's trailing
     * newline, is ignored.
     *
     * @throws IllegalArgumentException when the text is not Base64
     */
    public static SecretKey keyFromBase64(String encoded) {
        return new SecretKeySpec(Base64.getDecoder().decode(encoded.trim()), ALGORITHM);
    }

    /**
     * The stored form of a secret, with a fresh random IV every time (equal
     * secrets must not produce equal values). Null and empty stay as they are
     * (absence is not a secret), and a value that is already encrypted is
     * returned unchanged, so encrypting twice never double-wraps.
     *
     * @throws IllegalStateException when the value cannot be encrypted (an
     *         unusable key); the plaintext is then never returned in its place
     */
    public static String encrypt(SecretKey key, String plaintext) {
        if (plaintext == null || plaintext.isEmpty() || isEncrypted(plaintext)) return plaintext;
        try {
            byte[] iv = new byte[IV_BYTES];
            RANDOM.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));
            byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            byte[] combined = new byte[IV_BYTES + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, IV_BYTES);
            System.arraycopy(encrypted, 0, combined, IV_BYTES, encrypted.length);
            return PREFIX + Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new IllegalStateException("Could not encrypt a stored secret: " + e.getMessage(), e);
        }
    }

    /**
     * The usable form of a stored value. A value without the marker (written
     * before encryption existed) and null or empty are returned unchanged.
     *
     * @throws IllegalArgumentException when a marked value cannot be decrypted
     *         with this key: a different or changed key, or a damaged value.
     *         Callers turn this into their own message, since only they know
     *         where their key comes from.
     */
    public static String decrypt(SecretKey key, String stored) {
        if (stored == null || stored.isEmpty() || !isEncrypted(stored)) return stored;
        try {
            byte[] combined = Base64.getDecoder().decode(stored.substring(PREFIX.length()));
            if (combined.length <= IV_BYTES) throw new IllegalArgumentException("the value is truncated");
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, combined, 0, IV_BYTES));
            return new String(cipher.doFinal(combined, IV_BYTES, combined.length - IV_BYTES), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException("A stored secret could not be decrypted with this key", e);
        }
    }
}

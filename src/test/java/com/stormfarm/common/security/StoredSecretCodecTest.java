package com.stormfarm.common.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The enc:v1 format storm-core writes and storm-authentication reads. Values
 * in existing databases were written with this format, so it must never
 * change; the fixed vector below is the guard.
 */
class StoredSecretCodecTest {

    /** Key bytes 0x00..0x1f, Base64 — as CASTFARM_SECRETS_KEY or the key file holds it. */
    private static final String VECTOR_KEY = "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8=";
    /** IV a0..ab, then AES-256-GCM ciphertext and 128-bit tag of {@link #VECTOR_PLAINTEXT}. */
    private static final String VECTOR_STORED =
            "enc:v1:oKGio6SlpqeoqaqrxnQYTDXmYNYMAaejZl7kqR/ePT+5imKvIM25plGmy0FxEAHYXye3IuKTkdU=";
    private static final String VECTOR_PLAINTEXT = " ldap-bind pa$$word/+= üß ";

    private static SecretKey randomKey() {
        byte[] raw = new byte[StoredSecretCodec.KEY_BYTES];
        new SecureRandom().nextBytes(raw);
        return StoredSecretCodec.keyFromBase64(Base64.getEncoder().encodeToString(raw));
    }

    @Test
    @DisplayName("A value stored in the v1 format decrypts with its key (fixed vector)")
    void fixedVectorDecrypts() {
        SecretKey key = StoredSecretCodec.keyFromBase64(VECTOR_KEY);
        assertTrue(StoredSecretCodec.isEncrypted(VECTOR_STORED));
        assertEquals(VECTOR_PLAINTEXT, StoredSecretCodec.decrypt(key, VECTOR_STORED));
    }

    @Test
    @DisplayName("What the codec writes is the v1 layout: prefix, 12-byte IV, GCM with a 128-bit tag")
    void encryptWritesTheV1Layout() throws Exception {
        byte[] raw = Base64.getDecoder().decode(VECTOR_KEY);
        String stored = StoredSecretCodec.encrypt(StoredSecretCodec.keyFromBase64(VECTOR_KEY), "layout-check");

        assertTrue(stored.startsWith("enc:v1:"));
        byte[] combined = Base64.getDecoder().decode(stored.substring("enc:v1:".length()));
        // Decrypted here with the JCE directly, independently of the codec.
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(raw, "AES"), new GCMParameterSpec(128, combined, 0, 12));
        assertEquals("layout-check",
                new String(cipher.doFinal(combined, 12, combined.length - 12), StandardCharsets.UTF_8));
        assertEquals(12 + "layout-check".length() + 16, combined.length);
    }

    @Test
    @DisplayName("A secret survives the round trip unchanged")
    void roundTrip() {
        SecretKey key = randomKey();
        String token = "ATATT3xFfGF0-a-jira-token-with-symbols/+=";
        String stored = StoredSecretCodec.encrypt(key, token);
        assertNotEquals(token, stored);
        assertTrue(StoredSecretCodec.isEncrypted(stored));
        assertEquals(token, StoredSecretCodec.decrypt(key, stored));
    }

    @Test
    @DisplayName("The same secret encrypts differently every time")
    void ivIsPerValue() {
        SecretKey key = randomKey();
        assertNotEquals(StoredSecretCodec.encrypt(key, "same"), StoredSecretCodec.encrypt(key, "same"));
    }

    @Test
    @DisplayName("Unmarked, empty and null values pass through; encrypting twice does not double-wrap")
    void passThrough() {
        SecretKey key = randomKey();
        assertEquals("an-old-plaintext-password", StoredSecretCodec.decrypt(key, "an-old-plaintext-password"));
        assertFalse(StoredSecretCodec.isEncrypted("an-old-plaintext-password"));
        assertFalse(StoredSecretCodec.isEncrypted(null));
        assertNull(StoredSecretCodec.encrypt(key, null));
        assertEquals("", StoredSecretCodec.encrypt(key, ""));
        assertNull(StoredSecretCodec.decrypt(key, null));
        assertEquals("", StoredSecretCodec.decrypt(key, ""));
        String once = StoredSecretCodec.encrypt(key, "secret");
        assertEquals(once, StoredSecretCodec.encrypt(key, once));
    }

    @Test
    @DisplayName("A different key, a damaged value or no key is an error, never garbage")
    void unreadableValuesThrow() {
        assertThrows(IllegalArgumentException.class, () -> StoredSecretCodec.decrypt(randomKey(), VECTOR_STORED));
        SecretKey key = StoredSecretCodec.keyFromBase64(VECTOR_KEY);
        assertThrows(IllegalArgumentException.class, () -> StoredSecretCodec.decrypt(key, "enc:v1:not-base64!"));
        assertThrows(IllegalArgumentException.class, () -> StoredSecretCodec.decrypt(key, "enc:v1:AAECAw=="));
        String tampered = VECTOR_STORED.substring(0, VECTOR_STORED.length() - 4) + "AAA=";
        assertThrows(IllegalArgumentException.class, () -> StoredSecretCodec.decrypt(key, tampered));
        assertThrows(IllegalArgumentException.class, () -> StoredSecretCodec.decrypt(null, VECTOR_STORED));
    }

    @Test
    @DisplayName("A key that is not Base64 is refused; a key file's trailing newline is not")
    void keyDecoding() {
        assertThrows(IllegalArgumentException.class, () -> StoredSecretCodec.keyFromBase64("not base64!"));
        SecretKey key = StoredSecretCodec.keyFromBase64(VECTOR_KEY + "\n");
        assertEquals(VECTOR_PLAINTEXT, StoredSecretCodec.decrypt(key, VECTOR_STORED));
    }

    @Test
    @DisplayName("Settings ending in password, token or secret hold credentials; others do not")
    void secretSettingKeys() {
        assertTrue(StoredSecretCodec.isSecretKey("auth.ldap_bind_password"));
        assertTrue(StoredSecretCodec.isSecretKey("smtp.password"));
        assertTrue(StoredSecretCodec.isSecretKey("jira.api_token"));
        assertTrue(StoredSecretCodec.isSecretKey("bridge.secret"));
        assertFalse(StoredSecretCodec.isSecretKey("auth.ldap_user_search"));
        assertFalse(StoredSecretCodec.isSecretKey("auth.mode"));
        assertFalse(StoredSecretCodec.isSecretKey("password.min_length"));
        assertFalse(StoredSecretCodec.isSecretKey(null));
    }
}

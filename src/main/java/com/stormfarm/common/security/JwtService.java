package com.stormfarm.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtService(String privKeyPem, String pubKeyPem) {
        this.privateKey = privKeyPem != null ? loadPrivateKey(privKeyPem) : null;
        this.publicKey = pubKeyPem != null ? loadPublicKey(pubKeyPem) : null;
    }

    public String issueAccessToken(Long userId, String email, String username, List<String> roles, long expiryMs) {
        if (privateKey == null) throw new IllegalStateException("Private key not configured for signing");
        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("username", username)
                .claim("roles", roles)
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiryMs))
                .signWith(privateKey)
                .compact();
    }

    public String issueAccessToken(Long userId, String email, String username, List<String> roles) {
        return issueAccessToken(userId, email, username, roles, 15 * 60 * 1000L); // 15 mins default
    }

    public String issueRefreshToken(Long userId) {
        if (privateKey == null) throw new IllegalStateException("Private key not configured for signing");
        return Jwts.builder()
                .subject(userId.toString())
                .claim("type", "refresh")
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L)) // 7 days
                .signWith(privateKey)
                .compact();
    }

    public Claims validate(String token) {
        if (publicKey == null) throw new IllegalStateException("Public key not configured for validation");
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            validate(token);
            return true;
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    private PrivateKey loadPrivateKey(String pem) {
        if (pem == null || pem.trim().isEmpty()) return null;
        try {
            String privateKeyPEM = pem.replaceAll("-----BEGIN.*?-----", "").replaceAll("-----END.*?-----", "").replace("\\n", "").replaceAll("[^A-Za-z0-9+/=]", "");
            if (privateKeyPEM.isEmpty()) return null;
            byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(encoded));
        } catch (Exception e) { throw new RuntimeException("Failed to load JWT private key", e); }
    }

    private PublicKey loadPublicKey(String pem) {
        if (pem == null || pem.trim().isEmpty()) return null;
        try {
            String publicKeyPEM = pem.replaceAll("-----BEGIN.*?-----", "").replaceAll("-----END.*?-----", "").replace("\\n", "").replaceAll("[^A-Za-z0-9+/=]", "");
            if (publicKeyPEM.isEmpty()) return null;
            byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encoded));
        } catch (Exception e) { throw new RuntimeException("Failed to load JWT public key", e); }
    }
}

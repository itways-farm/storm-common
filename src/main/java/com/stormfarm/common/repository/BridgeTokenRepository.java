package com.stormfarm.common.repository;

import com.stormfarm.common.entity.BridgeToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface BridgeTokenRepository extends JpaRepository<BridgeToken, Long> {

    Optional<BridgeToken> findByToken(String token);

    /** Handshake check: is this an enabled, known token? */
    boolean existsByTokenAndEnabledTrue(String token);

    /** Refreshes last-seen for a connected bridge; drives online/offline status. */
    @Modifying
    @Transactional
    @Query("update BridgeToken b set b.lastSeenAt = :ts where b.token = :token")
    int touchLastSeen(@Param("token") String token, @Param("ts") Instant ts);
}

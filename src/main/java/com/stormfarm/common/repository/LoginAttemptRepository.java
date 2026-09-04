package com.stormfarm.common.repository;

import com.stormfarm.common.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    int countByEmailAndSuccessFalseAndAttemptedAtAfter(String email, Instant time);

    @Modifying
    @Query("DELETE FROM LoginAttempt l WHERE l.attemptedAt < :time")
    void deleteByAttemptedAtBefore(@Param("time") Instant time);
}

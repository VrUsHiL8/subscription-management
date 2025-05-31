package com.vrushil.subscription_management.repository;

import com.vrushil.subscription_management.entity.RefreshToken;
import com.vrushil.subscription_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.token = :token, rt.expiryDate = :expiryDate WHERE rt.user.id = :userId")
    void updateToken(@Param("userId") Long userId, @Param("token") String token, @Param("expiryDate") Instant expiryDate);

    void deleteByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);
}

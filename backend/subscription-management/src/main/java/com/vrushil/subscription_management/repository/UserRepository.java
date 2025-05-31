package com.vrushil.subscription_management.repository;

import com.vrushil.subscription_management.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(@NotNull @Email(message = "Email should be valid") String email);

    boolean existsByEmail(@NotNull @Email(message = "Email should be valid") String email);

    Optional<User> findByResetToken(String token);

    Optional<User> findByEmailOrPhone(String email, String phone);

    Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);

    @Query("SELECT u.userId FROM User u")
    List<Long> findAllUserIds();
}

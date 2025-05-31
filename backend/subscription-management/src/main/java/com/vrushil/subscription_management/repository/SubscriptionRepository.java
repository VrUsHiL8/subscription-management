package com.vrushil.subscription_management.repository;

import com.vrushil.subscription_management.entity.Subscription;
import com.vrushil.subscription_management.entity.SubscriptionStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Page<Subscription> findByUser_UserId(Long userId, Pageable pageable);
    boolean existsByUser_UserIdAndPlan_PlanId(@NotNull Long userId,  @NotNull Long planId);
    // Fetch upcoming renewals (subscriptions ending in the next 7 days)
    Page<Subscription> findByUser_UserIdAndEndDateBetween(
            Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    // Fetch expired subscriptions
    Page<Subscription> findByUser_UserIdAndStatus(
            Long userId, SubscriptionStatus status, Pageable pageable);
    // Fetch early renewals (subscriptions where endDate is beyond 7 days)
    Page<Subscription> findByUser_UserIdAndEndDateAfter(
            Long userId, LocalDate date, Pageable pageable);

    Page<Subscription> findAllByApp_AppNameContainingIgnoreCase(String search, Pageable pageable);
    Page<Subscription> findAllByCategory_CategoryTypeContainingIgnoreCase(String search, Pageable pageable);
    @Query("SELECT s FROM Subscription s WHERE LOWER(CAST(s.plan.planType AS string)) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Subscription> findAllByPlanTypeLike(@Param("search") String search, Pageable pageable);

    Page<Subscription> findByUser_UserIdAndApp_AppNameContainingIgnoreCase(Long userId, String search, Pageable pageable);

    Page<Subscription> findByUser_UserIdAndCategory_CategoryTypeContainingIgnoreCase(Long userId, String search, Pageable pageable);

    @Query("SELECT s FROM Subscription s WHERE s.user.userId = :userId AND LOWER(CAST(s.plan.planType AS string)) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Subscription> findByUser_UserIdAndPlan_PlanTypeContainingIgnoreCase(@Param("userId") Long userId, @Param("search") String search, Pageable pageable);

    // UPCOMING RENEWALS SEARCH
    Page<Subscription> findByUser_UserIdAndEndDateBetweenAndApp_AppNameContainingIgnoreCase(
            Long userId, LocalDate startDate, LocalDate endDate, String search, Pageable pageable);

    Page<Subscription> findByUser_UserIdAndEndDateBetweenAndCategory_CategoryTypeContainingIgnoreCase(
            Long userId, LocalDate startDate, LocalDate endDate, String search, Pageable pageable);

    @Query("SELECT s FROM Subscription s WHERE s.user.userId = :userId AND s.endDate BETWEEN :startDate AND :endDate AND LOWER(CAST(s.plan.planType AS string)) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Subscription> findByUser_UserIdAndEndDateBetweenAndPlan_PlanTypeContainingIgnoreCase(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("search") String search,
            Pageable pageable);

    // EXPIRED SUBSCRIPTIONS SEARCH
    Page<Subscription> findByUser_UserIdAndStatusAndApp_AppNameContainingIgnoreCase(
            Long userId, SubscriptionStatus status, String search, Pageable pageable);

    Page<Subscription> findByUser_UserIdAndStatusAndCategory_CategoryTypeContainingIgnoreCase(
            Long userId, SubscriptionStatus status, String search, Pageable pageable);

    @Query("SELECT s FROM Subscription s WHERE s.user.userId = :userId AND s.status = :status AND LOWER(CAST(s.plan.planType AS string)) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Subscription> findByUser_UserIdAndStatusAndPlan_PlanTypeContainingIgnoreCase(
            @Param("userId") Long userId,
            @Param("status") SubscriptionStatus status,
            @Param("search") String search,
            Pageable pageable);

    // EARLY RENEWALS SEARCH
    Page<Subscription> findByUser_UserIdAndEndDateAfterAndApp_AppNameContainingIgnoreCase(
            Long userId, LocalDate date, String search, Pageable pageable);

    Page<Subscription> findByUser_UserIdAndEndDateAfterAndCategory_CategoryTypeContainingIgnoreCase(
            Long userId, LocalDate date, String search, Pageable pageable);

    @Query("SELECT s FROM Subscription s WHERE s.user.userId = :userId AND s.endDate > :date AND LOWER(CAST(s.plan.planType AS string)) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Subscription> findByUser_UserIdAndEndDateAfterAndPlan_PlanTypeContainingIgnoreCase(
            @Param("userId") Long userId,
            @Param("date") LocalDate date,
            @Param("search") String search,
            Pageable pageable);

    List<Subscription> findByStatusAndEndDateBefore(SubscriptionStatus status, LocalDate date);
}

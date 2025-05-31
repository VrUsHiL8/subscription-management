package com.vrushil.subscription_management.repository;

import com.vrushil.subscription_management.entity.Renewal;
import com.vrushil.subscription_management.entity.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RenewalRepository extends JpaRepository<Renewal, Long> {

    Page<Renewal> findBySubscription_User_UserId(Long userId, Pageable pageable);
    boolean existsBySubscription_SubscriptionIdAndStatus(Long subscriptionId, TransactionStatus status);

    @Query("SELECT r FROM Renewal r WHERE LOWER(CAST(r.plan.planType AS string)) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Renewal> findAllByPlanTypeLike(@Param("search") String search, Pageable pageable);
    Page<Renewal> findByPlan_App_AppNameContainingIgnoreCase(String search, Pageable pageable);
    @Query("SELECT r FROM Renewal r WHERE LOWER(CAST(r.status AS string)) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Renewal> findByStatusLike(@Param("search") String search, Pageable pageable);

    @Query("SELECT r FROM Renewal r WHERE r.user.userId = :userId AND LOWER(CAST(r.status AS string)) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Renewal> findByUser_UserIdAndStatusLike(Long userId, String status, Pageable pageable);
    Page<Renewal> findByUser_UserIdAndPlan_App_AppNameContainingIgnoreCase(Long userId, String appName, Pageable pageable);
    @Query("SELECT r FROM Renewal r WHERE r.user.userId = :userId AND LOWER(CAST(r.plan.planType AS string)) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Renewal> findByUser_UserIdAndPlan_PlanTypeContainingIgnoreCase(Long userId, String planType, Pageable pageable);
    Page<Renewal> findByUser_UserId(Long userId, Pageable pageable);

}

package com.vrushil.subscription_management.repository;

import com.vrushil.subscription_management.entity.Plan;
import com.vrushil.subscription_management.entity.PlanType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlanRepository extends JpaRepository<Plan,Long>{
    Plan findByPlanType(PlanType planType);
    Page<Plan> findByApp_AppId(Pageable pageable, Long appId);

    Page<Plan> findAllByApp_AppNameContainingIgnoreCase(String search, Pageable pageable);
    Page<Plan> findAllByApp_Category_CategoryTypeContainingIgnoreCase(String search, Pageable pageable);
    @Query("SELECT p FROM Plan p WHERE LOWER(CAST(p.planType AS string)) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Plan> findAllByPlanTypeLike(@Param("search") String search, Pageable pageable);
}

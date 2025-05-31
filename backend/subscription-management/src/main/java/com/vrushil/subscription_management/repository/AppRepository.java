package com.vrushil.subscription_management.repository;

import com.vrushil.subscription_management.entity.App;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends JpaRepository<App, Long> {
    boolean existsByAppName(String appName);
    Page<App> findByCategory_CategoryId(Pageable pageable, Long categoryId);
    Page<App> findByAppNameContainingIgnoreCase(String search, Pageable pageable);
    Page<App> findByCategory_CategoryTypeContainingIgnoreCase(String search, Pageable pageable);
}

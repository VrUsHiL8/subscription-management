package com.vrushil.subscription_management.repository;

import com.vrushil.subscription_management.entity.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryType(String categoryType);
    Page<Category> findByCategoryTypeContainingIgnoreCase(String search, Pageable pageable);

}

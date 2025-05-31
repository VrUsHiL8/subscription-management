package com.vrushil.subscription_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "plans")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "plan_id")
        private Long planId;

        @ManyToOne
        @JoinColumn(name = "app_id", nullable = false)
        @JsonIgnoreProperties("plans")
        private App app;

        @Enumerated(EnumType.STRING)
        @Column(name = "billing_cycle", nullable = false)
        private BillingCycle billingCycle;

        @NotNull
        @Column(name = "price", nullable = false, precision = 10, scale = 2)
        private BigDecimal price;

        @NotNull
        @Column(name = "features", columnDefinition = "TEXT")
        private String features;

        @Enumerated(EnumType.STRING)
        @Column(name = "plan_type", nullable = false)
        private PlanType planType;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDate createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDate updatedAt;
}
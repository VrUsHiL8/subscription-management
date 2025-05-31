package com.vrushil.subscription_management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "subscriptions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Renewal> renewals;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-subscriptions")
    private User user;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    @JsonBackReference("plan-subscriptions")
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "app_id", nullable = false)
    @JsonBackReference
    private App app;

    // Utility methods for easy access
    public String getAppName() {
        return app != null ? app.getAppName() : null;
    }

    public String getCategoryType() {
        return app!=null && app.getCategory() != null ? app.getCategory().getCategoryType() : null;
    }

    public PlanType getPlanType() {
        return plan != null ? plan.getPlanType() : null;
    }

    public BigDecimal getPrice() {
        return plan != null ? plan.getPrice() : null;
    }

}

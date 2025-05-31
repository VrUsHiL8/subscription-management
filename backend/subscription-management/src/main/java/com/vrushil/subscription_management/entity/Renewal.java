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
@Table(name = "renewals")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Renewal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long renewalId;

    @NotNull
    private LocalDate renewalDate;

    @NotNull
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal renewalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(name = "initiated_early")
    private Boolean initiatedEarly = false;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    @JsonIgnoreProperties({"renewals", "user"})
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"subscriptions", "password"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    @JsonIgnoreProperties({"subscriptions"})
    private Plan plan;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    public String getAppName() {
        return subscription != null && subscription.getApp() != null ? subscription.getApp().getAppName() : null;
    }

    public String getPlanType() {
        return subscription != null && subscription.getPlan() != null ? subscription.getPlan().getPlanType().name() : null;
    }

    public String getBillingCycle() {
        return subscription != null && subscription.getPlan() != null ? subscription.getPlan().getBillingCycle().name() : null;
    }
}


package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.entity.Subscription;
import com.vrushil.subscription_management.entity.SubscriptionStatus;
import com.vrushil.subscription_management.repository.SubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SubscriptionStatusUpdater {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionStatusUpdater(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    // Runs every day at midnight
    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void expireOldSubscriptions() {
        LocalDate today = LocalDate.now();

        List<Subscription> expiring = subscriptionRepository
                .findByStatusAndEndDateBefore(SubscriptionStatus.ACTIVE, today);

        for (Subscription sub : expiring) {
            sub.setStatus(SubscriptionStatus.EXPIRED);
        }

        subscriptionRepository.saveAll(expiring);
    }
}
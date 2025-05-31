package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RenewalNotificationScheduler {

    private final RenewalServiceImpl renewalService;
    private final UserRepository userRepository;
    public RenewalNotificationScheduler(RenewalServiceImpl renewalService,
                                        UserRepository userRepository) {
        this.renewalService = renewalService;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 25 15 * * MON")// Every Monday at 8 AM
    public void sendWeeklyRenewalSummary() {
        List<Long> userIds = userRepository.findAllUserIds(); // Fetch all user IDs
        for (Long userId : userIds) {
            renewalService.sendRenewalNotifications(userId);
        }
    }
}

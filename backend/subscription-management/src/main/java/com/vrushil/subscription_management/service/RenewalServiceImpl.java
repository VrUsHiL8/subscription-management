package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.entity.*;
import com.vrushil.subscription_management.exception.PlanNotFoundException;
import com.vrushil.subscription_management.exception.RenewalNotFoundException;
import com.vrushil.subscription_management.exception.SubscriptionNotFoundException;
import com.vrushil.subscription_management.repository.RenewalRepository;
import com.vrushil.subscription_management.repository.SubscriptionRepository;
import com.vrushil.subscription_management.repository.UserRepository;
import com.vrushil.subscription_management.request.RenewalRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.RenewalResponse;
import com.vrushil.subscription_management.response.SubscriptionResponse;
import com.vrushil.subscription_management.util.Mapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
@Slf4j
@Service
public class RenewalServiceImpl implements RenewalService {

    private final RenewalRepository renewalRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final GmailService gmailService;
    private final UserRepository userRepository;

    public RenewalServiceImpl(RenewalRepository renewalRepository,
                              SubscriptionRepository subscriptionRepository,
                              GmailService gmailService,
                              UserRepository userRepository) {
        this.renewalRepository = renewalRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.gmailService = gmailService;
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponse<RenewalResponse> createRenewal(RenewalRequest request) {
        Subscription subscription = subscriptionRepository.findById(request.getSubscriptionId())
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));

        Plan plan = subscription.getPlan();

        if (plan == null || plan.getPlanType() == null || plan.getBillingCycle() == null) {
            throw new RuntimeException("Invalid subscription plan details.");
        }
        if (request.getRenewalDate() != null && request.getRenewalDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Renewal date cannot be in the past.");
        }
        Renewal renewal = new Renewal();
        renewal.setSubscription(subscription);
        renewal.setUser(subscription.getUser());
        renewal.setPlan(plan);
        renewal.setRenewalDate(request.getRenewalDate() != null ? request.getRenewalDate() : LocalDate.now());
        renewal.setRenewalPrice(plan.getPrice());
        renewal.setStatus(TransactionStatus.PENDING);


        Renewal savedRenewal = renewalRepository.save(renewal);
        return new ApiResponse<>("SUCCESS", "Renewal created successfully", Mapper.toRenewalResponse(savedRenewal));
    }

    @Override
    public ApiResponse<RenewalResponse> getRenewalById(Long renewalId) {
        Renewal renewal = renewalRepository.findById(renewalId)
                .orElseThrow(() -> new RenewalNotFoundException("Renewal not found"));
        return new ApiResponse<>("SUCCESS", "Renewal retrieved successfully", Mapper.toRenewalResponse(renewal));
    }

    @Override
    public ApiResponse<Page<RenewalResponse>> getAllRenewals(String search, Pageable pageable) {
        Page<Renewal> renewals;
        if (search != null && !search.isBlank()) {
            renewals = renewalRepository.findByStatusLike(search, pageable);

            if (renewals.isEmpty()) {
                renewals = renewalRepository.findByPlan_App_AppNameContainingIgnoreCase(search, pageable);
            }

            if (renewals.isEmpty()) {
                renewals = renewalRepository.findAllByPlanTypeLike(search, pageable);
            }
        } else {
            renewals = renewalRepository.findAll(pageable);
        }
        Page<RenewalResponse> responsePage = renewals
                .map(Mapper::toRenewalResponse);
        return new ApiResponse<>("SUCCESS", "All renewals retrieved", responsePage);
    }

    @Override
    public ApiResponse<Page<RenewalResponse>> getAllRenewalsForId(String search, Long userId, Pageable pageable) {
        Page<Renewal> renewals;

        if (search != null && !search.isBlank()) {
            // 1. Try searching by Status
            renewals = renewalRepository.findByUser_UserIdAndStatusLike(userId, search, pageable);

            // 2. Try searching by App Name
            if (renewals.isEmpty()) {
                renewals = renewalRepository.findByUser_UserIdAndPlan_App_AppNameContainingIgnoreCase(userId, search, pageable);
            }

            // 3. Try searching by Plan Type
            if (renewals.isEmpty()) {
                renewals = renewalRepository.findByUser_UserIdAndPlan_PlanTypeContainingIgnoreCase(userId, search, pageable);
            }
        } else {
            // No search → fetch all renewals for user
            renewals = renewalRepository.findByUser_UserId(userId, pageable);
        }

        Page<RenewalResponse> responseList = renewals.map(Mapper::toRenewalResponse);
        return new ApiResponse<>("SUCCESS", "Renewals for user retrieved", responseList);
    }

    @Override
    public ApiResponse<String> deleteRenewal(Long id) {
        Renewal renewal = renewalRepository.findById(id)
                .orElseThrow(() -> new RenewalNotFoundException("Renewal not found"));

        if (renewal.getStatus() == TransactionStatus.SUCCESS) {
            throw new RuntimeException("Cannot delete a successful renewal.");
        }

        renewalRepository.deleteById(id);
        return new ApiResponse<>("SUCCESS", "Renewal deleted successfully", "Renewal with ID: " + id + " deleted");
    }

    private LocalDate calculateNewEndDate(LocalDate currentEndDate, BillingCycle cycle) {
        if (currentEndDate == null) {
            currentEndDate = LocalDate.now();
        }
        return switch (cycle) {
            case MONTHLY -> currentEndDate.plusMonths(1);
            case QUARTERLY -> currentEndDate.plusMonths(3);
            case YEARLY -> currentEndDate.plusYears(1);
            default -> throw new IllegalArgumentException("Unsupported billing cycle: " + cycle);
        };
    }

    @Override
    @Transactional
    public ApiResponse<String> approveRenewal(Long renewalId) {
        Renewal renewal = renewalRepository.findById(renewalId)
                .orElseThrow(() -> new RenewalNotFoundException("Renewal not found"));

        if (!renewal.getStatus().equals(TransactionStatus.PENDING)) {
            throw new RuntimeException("Only PENDING renewals can be approved.");
        }

        renewal.setStatus(TransactionStatus.SUCCESS);
        Subscription subscription = renewal.getSubscription();
        LocalDate currentEndDate = subscription.getEndDate();
        LocalDate approvalDate = LocalDate.now();

        // Set start date of new cycle: after current end date (if in future), or today (if expired)
        LocalDate renewalStartDate = currentEndDate.isAfter(approvalDate) ? currentEndDate : approvalDate;
        LocalDate newEndDate = calculateNewEndDate(renewalStartDate, renewal.getPlan().getBillingCycle());

        // Update subscription
        subscription.setStartDate(renewalStartDate);
        subscription.setEndDate(newEndDate);

        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscriptionRepository.save(subscription);
        renewalRepository.save(renewal);

        return new ApiResponse<>("SUCCESS", "Renewal approved and subscription extended", "Renewal ID: " + renewalId);
    }

    @Override
    public ApiResponse<Page<SubscriptionResponse>> getUpcomingRenewalsForUser(String search, Long userId, Pageable pageable) {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        Page<Subscription> upcomingSubscriptions;

        if (search != null && !search.isBlank()) {
            upcomingSubscriptions = subscriptionRepository.findByUser_UserIdAndEndDateBetweenAndApp_AppNameContainingIgnoreCase(userId, today, nextWeek, search, pageable);
            if (upcomingSubscriptions.isEmpty()) {
                upcomingSubscriptions = subscriptionRepository.findByUser_UserIdAndEndDateBetweenAndCategory_CategoryTypeContainingIgnoreCase(userId, today, nextWeek, search, pageable);
            }
            if (upcomingSubscriptions.isEmpty()) {
                upcomingSubscriptions = subscriptionRepository.findByUser_UserIdAndEndDateBetweenAndPlan_PlanTypeContainingIgnoreCase(userId, today, nextWeek, search, pageable);
            }
        } else {
            upcomingSubscriptions = subscriptionRepository.findByUser_UserIdAndEndDateBetween(userId, today, nextWeek, pageable);
        }

        Page<SubscriptionResponse> responsePage = upcomingSubscriptions.map(Mapper::toSubscriptionResponse);
        return new ApiResponse<>("SUCCESS", "Upcoming renewals fetched successfully", responsePage);
    }

    @Override
    public ApiResponse<Page<SubscriptionResponse>> getExpiredRenewalsForUser(String search, Long userId, Pageable pageable) {
        Page<Subscription> expiredSubscriptions;

        if (search != null && !search.isBlank()) {
            expiredSubscriptions = subscriptionRepository.findByUser_UserIdAndStatusAndApp_AppNameContainingIgnoreCase(userId, SubscriptionStatus.EXPIRED, search, pageable);
            if (expiredSubscriptions.isEmpty()) {
                expiredSubscriptions = subscriptionRepository.findByUser_UserIdAndStatusAndCategory_CategoryTypeContainingIgnoreCase(userId, SubscriptionStatus.EXPIRED, search, pageable);
            }
            if (expiredSubscriptions.isEmpty()) {
                expiredSubscriptions = subscriptionRepository.findByUser_UserIdAndStatusAndPlan_PlanTypeContainingIgnoreCase(userId, SubscriptionStatus.EXPIRED, search, pageable);
            }
        } else {
            expiredSubscriptions = subscriptionRepository.findByUser_UserIdAndStatus(userId, SubscriptionStatus.EXPIRED, pageable);
        }

        Page<SubscriptionResponse> responsePage = expiredSubscriptions.map(Mapper::toSubscriptionResponse);
        return new ApiResponse<>("SUCCESS", "Expired renewals fetched successfully", responsePage);
    }

    @Override
    public ApiResponse<Page<SubscriptionResponse>> getEarlyRenewalsForUser(String search, Long userId, Pageable pageable) {
        LocalDate nextWeek = LocalDate.now().plusDays(7);

        Page<Subscription> earlySubscriptions;

        if (search != null && !search.isBlank()) {
            earlySubscriptions = subscriptionRepository.findByUser_UserIdAndEndDateAfterAndApp_AppNameContainingIgnoreCase(userId, nextWeek, search, pageable);
            if (earlySubscriptions.isEmpty()) {
                earlySubscriptions = subscriptionRepository.findByUser_UserIdAndEndDateAfterAndCategory_CategoryTypeContainingIgnoreCase(userId, nextWeek, search, pageable);
            }
            if (earlySubscriptions.isEmpty()) {
                earlySubscriptions = subscriptionRepository.findByUser_UserIdAndEndDateAfterAndPlan_PlanTypeContainingIgnoreCase(userId, nextWeek, search, pageable);
            }
        } else {
            earlySubscriptions = subscriptionRepository.findByUser_UserIdAndEndDateAfter(userId, nextWeek, pageable);
        }

        Page<SubscriptionResponse> responsePage = earlySubscriptions.map(Mapper::toSubscriptionResponse);
        return new ApiResponse<>("SUCCESS", "Early renewals fetched successfully", responsePage);
    }


    public void sendRenewalNotifications(Long userId) {
        // Get upcoming renewals page (no search, unpaged)
        ApiResponse<Page<SubscriptionResponse>> response = getUpcomingRenewalsForUser("", userId, Pageable.unpaged());

        Page<SubscriptionResponse> upcomingRenewals = response.getData();

        // Only proceed if there are upcoming renewals
        if (upcomingRenewals == null || upcomingRenewals.isEmpty()) {
            log.info("No upcoming renewals for user ID: {}", userId);
            return;
        }

        // Get user email using userId
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String userEmail = user.getEmail();

            // Send the email
            String subject = "Upcoming Subscription Renewals";
            gmailService.sendRenewalSummaryEmail(userEmail, subject, upcomingRenewals.getContent());
        } else {
            log.error("User with ID {} not found", userId);
        }
    }
}

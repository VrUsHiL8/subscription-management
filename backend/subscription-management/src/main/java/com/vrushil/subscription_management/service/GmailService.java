package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.response.SubscriptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GmailService {

    private final JavaMailSender javaMailSender;

    public GmailService(JavaMailSender javaMailSender){
        this.javaMailSender=javaMailSender;
    }

    public void sendRenewalSummaryEmail(String to, String subject, List<SubscriptionResponse> upcomingRenewals) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);

            StringBuilder body = new StringBuilder();
            body.append("Hello,\n\nHere are your upcoming subscription renewals:\n\n");

            // Top 3 upcoming renewals
            int counter = 0;
            for (SubscriptionResponse renewal : upcomingRenewals) {
                counter++;
                body.append(String.format("%d. %s (%s) - %s, Renewal Price: $%.2f\n", counter,
                        renewal.getAppName(), renewal.getPlanType(), renewal.getEndDate(), renewal.getPrice()));
                if (counter >= 3) break;  // Limit to top 3
            }

            // Link to view all renewals
            body.append("\n\nYou have more upcoming renewals. To view all, please log in: ");
            body.append("http://localhost:4200/login?redirect=/dashboard/renewals");

            mail.setText(body.toString());
            javaMailSender.send(mail);
        } catch (Exception e) {
            log.error("Exception while sending renewal summary email", e);
        }
    }
}

package com.vrushil.subscription_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
//@EnableTransactionManagement
@SpringBootApplication
public class SubscriptionManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionManagementApplication.class, args);
	}

}

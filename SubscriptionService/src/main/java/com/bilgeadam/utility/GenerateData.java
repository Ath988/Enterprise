package com.bilgeadam.utility;

import com.bilgeadam.entity.Subscription;
import com.bilgeadam.entity.enums.SubscriptionPlan;
import com.bilgeadam.entity.enums.SubscriptionStatus;
import com.bilgeadam.entity.enums.SubscriptionType;
import com.bilgeadam.repository.SubscriptionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GenerateData {
    private final SubscriptionRepository subscriptionRepository;

    @PostConstruct
    public void init() {
        if(subscriptionRepository.count() == 0) {
            Subscription subscription = Subscription.builder()
                    .userId(1L)
                    .subscriptionPlan(SubscriptionPlan.ENTERPRISE)
                    .subscriptionType(SubscriptionType.DEFAULT)
                    .subscriptionStatus(SubscriptionStatus.ACTIVE)
                    .startDate(System.currentTimeMillis())
                    .estimatedEndDate(System.currentTimeMillis()+(90*24*60*60*1000L)) //90 gün sonra
                    .build();
            subscriptionRepository.save(subscription);
        }
    }
}

package com.bilgeadam.subscriptionservice.service;

import com.bilgeadam.subscriptionservice.entity.Subscription;
import com.bilgeadam.subscriptionservice.entity.enums.EntityStatus;
import com.bilgeadam.subscriptionservice.entity.enums.SubscriptionStatus;
import com.bilgeadam.subscriptionservice.entity.enums.SubscriptionType;
import com.bilgeadam.subscriptionservice.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public Subscription addSubscription(String userId, SubscriptionType subscriptionType) {
        Subscription subscription = Subscription.builder()
                .subscriptionStatus(SubscriptionStatus.DEFAULT)
                .subscriptionType(subscriptionType)
                .userId(userId)
                .startDate(System.currentTimeMillis()) // right now
                .estimatedEndDate(System.currentTimeMillis() + (30*24*60*60*1000L)) // 30 days later
                .build();
        return subscriptionRepository.save(subscription);
    }

    public Subscription getCurrentSubscription(String userId) {
        return subscriptionRepository.findByUserIdAndEntityStatus(userId, EntityStatus.ACTIVE);
    }
}

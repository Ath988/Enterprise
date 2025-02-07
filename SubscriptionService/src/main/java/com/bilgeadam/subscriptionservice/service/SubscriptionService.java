package com.bilgeadam.subscriptionservice.service;

import com.bilgeadam.subscriptionservice.entity.Subscription;
import com.bilgeadam.subscriptionservice.entity.enums.EntityStatus;
import com.bilgeadam.subscriptionservice.entity.enums.SubscriptionStatus;
import com.bilgeadam.subscriptionservice.entity.enums.SubscriptionPlan;
import com.bilgeadam.subscriptionservice.mapper.SubscriptionMapper;
import com.bilgeadam.subscriptionservice.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final Long SUBSCRIPTION_PERIOD = (30*24*60*60*1000L);

    public Subscription addSubscription(String userId, SubscriptionPlan subscriptionPlan) {
        Subscription subscription = Subscription.builder()
                .subscriptionStatus(SubscriptionStatus.DEFAULT)
                .subscriptionPlan(subscriptionPlan)
                .userId(userId)
                .startDate(System.currentTimeMillis()) // right now
                .estimatedEndDate(System.currentTimeMillis() + SUBSCRIPTION_PERIOD) // 30 days later
                .build();
        return subscriptionRepository.save(subscription);
    }

    public Subscription getCurrentSubscription(String userId) {
        Optional<Subscription> optSubscription = subscriptionRepository
                .findTopByUserIdAndEntityStatusAndEstimatedEndDateGreaterThanEqual(userId, EntityStatus.ACTIVE, System.currentTimeMillis());
        if (optSubscription.isPresent()) {
            return optSubscription.get();
        }
        // TODO hata yönetimi için özel hata tanımları
        else throw new RuntimeException();
    }

    public List<Subscription> getSubscriptionHistory(String userId) {
        return subscriptionRepository.findAllByUserIdAndEntityStatus(userId, EntityStatus.ACTIVE);
    }

    public List<Subscription> updateSubscriptionPlan(String userId, SubscriptionPlan subscriptionPlan) {
        Subscription currentSubscription = getCurrentSubscription(userId);
        if (currentSubscription.getSubscriptionPlan().equals(subscriptionPlan)) {
            throw new RuntimeException();         // TODO hata yönetimi için özel hata tanımları
        }
        int grade = subscriptionPlan.compareTo(currentSubscription.getSubscriptionPlan());

        Subscription newSubscription = SubscriptionMapper.INSTANCE.subscriptionToSubscription(currentSubscription);
        newSubscription.setSubscriptionPlan(subscriptionPlan);
        newSubscription.setRelatedSubscriptionId(currentSubscription.getId());

        if (grade < 0) {
            newSubscription.setSubscriptionStatus(SubscriptionStatus.UPGRADED);
            currentSubscription.setSubscriptionStatus(SubscriptionStatus.UPGRADED_FROM);
        }
        else if (grade > 0) {
            newSubscription.setSubscriptionStatus(SubscriptionStatus.DOWNGRADED);
            currentSubscription.setSubscriptionStatus(SubscriptionStatus.DOWNGRADED_FROM);
        }

        newSubscription = subscriptionRepository.save(newSubscription);

        currentSubscription.setRelatedSubscriptionId(newSubscription.getId());
        currentSubscription = subscriptionRepository.save(currentSubscription);
    }
}

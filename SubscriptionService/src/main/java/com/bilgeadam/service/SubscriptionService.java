package com.bilgeadam.service;

import com.bilgeadam.entity.Subscription;
import com.bilgeadam.entity.enums.EntityStatus;
import com.bilgeadam.entity.enums.SubscriptionStatus;
import com.bilgeadam.entity.enums.SubscriptionPlan;
import com.bilgeadam.mapper.SubscriptionMapper;
import com.bilgeadam.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final Long SUBSCRIPTION_PERIOD = (30*24*60*60*1000L);

    public Subscription addSubscription(AddSubscriptionRequest dto) {
        Subscription subscription = Subscription.builder()
                .subscriptionStatus(SubscriptionStatus.DEFAULT)
                .subscriptionPlan(dto.subscriptionPlan())
                .userId(dto.userId())
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
        else throw new EnterpriseException(ErrorType.NO_ACTIVE_SUBSCRIPTION);
    }

    public List<Subscription> getSubscriptionHistory(String userId) {
        return subscriptionRepository.findAllByUserIdAndEntityStatus(userId, EntityStatus.ACTIVE);
    }

    public Subscription updateSubscriptionPlan(ChangeSubscriptionPlanRequest dto) {
        Subscription currentSubscription = getCurrentSubscription(dto.userId());
        if (currentSubscription.getSubscriptionPlan().equals(dto.subscriptionPlan())) {
            throw new EnterpriseException(ErrorType.SAME_SUBSCRIPTION_PLAN);
        }
        int grade = dto.subscriptionPlan().compareTo(currentSubscription.getSubscriptionPlan());

        Subscription newSubscription = SubscriptionMapper.INSTANCE.subscriptionToSubscription(currentSubscription);
        newSubscription.setSubscriptionPlan(dto.subscriptionPlan());
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

        return currentSubscription;

    }
}

package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddSubscriptionRequest;
import com.bilgeadam.dto.request.ChangeSubscriptionPlanRequest;
import com.bilgeadam.entity.Subscription;
import com.bilgeadam.entity.enums.EntityStatus;
import com.bilgeadam.entity.enums.SubscriptionStatus;
import com.bilgeadam.exception.EnterpriseException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.SubscriptionMapper;
import com.bilgeadam.repository.SubscriptionRepository;
import com.bilgeadam.utility.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final Long SUBSCRIPTION_PERIOD = (30*24*60*60*1000L);
    private final JwtManager jwtManager = new JwtManager();

    private Long tokenToUserId(String token){
        Optional<Long> optUserId = jwtManager.validateToken(token);
        if (optUserId.isEmpty()) {
            throw new EnterpriseException(ErrorType.INVALID_TOKEN);
        }
        return optUserId.get();
    }

    public Subscription addSubscription(AddSubscriptionRequest dto) {
        Long userId = tokenToUserId(dto.token());

        Subscription subscription = Subscription.builder()
                .subscriptionStatus(SubscriptionStatus.DEFAULT)
                .subscriptionPlan(dto.subscriptionPlan())
                .userId(userId)
                .startDate(System.currentTimeMillis()) // right now
                .estimatedEndDate(System.currentTimeMillis() + SUBSCRIPTION_PERIOD) // 30 days later
                .build();
        return subscriptionRepository.save(subscription);
    }

    public Subscription getCurrentSubscription(String token) {
        Long userId = tokenToUserId(token);

        Optional<Subscription> optSubscription = subscriptionRepository
                .findTopByUserIdAndStatusAndEstimatedEndDateGreaterThanEqual(userId, EntityStatus.ACTIVE, System.currentTimeMillis());
        if (optSubscription.isPresent()) {
            return optSubscription.get();
        }
        else throw new EnterpriseException(ErrorType.NO_ACTIVE_SUBSCRIPTION);
    }

    public List<Subscription> getSubscriptionHistory(String token) {
        Long userId = tokenToUserId(token);
        return subscriptionRepository.findAllByUserIdAndStatus(userId, EntityStatus.ACTIVE);
    }

    public Subscription updateSubscriptionPlan(ChangeSubscriptionPlanRequest dto) {


        Subscription currentSubscription = getCurrentSubscription(dto.token());
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

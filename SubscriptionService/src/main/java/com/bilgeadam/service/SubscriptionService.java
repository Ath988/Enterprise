package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddSubscriptionRequest;
import com.bilgeadam.dto.request.ChangeSubscriptionPlanRequest;
import com.bilgeadam.entity.Subscription;
import com.bilgeadam.entity.enums.EntityStatus;
import com.bilgeadam.entity.enums.SubscriptionPlan;
import com.bilgeadam.entity.enums.SubscriptionStatus;
import com.bilgeadam.entity.enums.SubscriptionType;
import com.bilgeadam.exception.EnterpriseException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.SubscriptionMapper;
import com.bilgeadam.repository.SubscriptionRepository;
import com.bilgeadam.utility.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
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
        Optional<Long> optAuthId = jwtManager.validateToken(token);
        if (optAuthId.isEmpty()) {
            throw new EnterpriseException(ErrorType.INVALID_TOKEN);
        }
        return subscriptionRepository.findUserIdFromAuthId(optAuthId.get());
    }


    @Scheduled(cron= "0 0 0 * * *")
    private void updateSubscriptionStatus(){
        subscriptionRepository.saveAll(subscriptionRepository.findAll().stream().map(subscription -> {
            if (subscription.getEstimatedEndDate() < System.currentTimeMillis()) {
                subscription.setSubscriptionStatus(SubscriptionStatus.OLD);
            }
            return subscription;
        }).toList());
    }

    public Boolean addSubscription(AddSubscriptionRequest dto) {
        Long authId = jwtManager.validateToken(dto.token())
                .orElseThrow(() -> new EnterpriseException(ErrorType.INVALID_TOKEN));

        Subscription subscription = Subscription.builder()
                .subscriptionType(SubscriptionType.DEFAULT)
                .subscriptionStatus(SubscriptionStatus.ACTIVE)
                .subscriptionPlan(dto.subscriptionPlan())
                .userId(dto.userId())
                .authId(authId)
                .startDate(System.currentTimeMillis()) // right now
                .estimatedEndDate(System.currentTimeMillis() + SUBSCRIPTION_PERIOD) // 30 days later
                .build();
         subscriptionRepository.save(subscription);
         return true;
    }

    //UserManagement için yazıldı.
    public SubscriptionPlan getUserSubscriptionPlan(Long userId){
        Subscription subscription = subscriptionRepository
                .findTopByUserIdAndStatusAndEstimatedEndDateGreaterThanEqual(userId,EntityStatus.ACTIVE,System.currentTimeMillis())
                .orElseThrow(()->new EnterpriseException(ErrorType.NO_ACTIVE_SUBSCRIPTION));
        return subscription.getSubscriptionPlan();
    }


    public SubscriptionPlan getCurrentActiveSubscriptionPlan(String token){
        Subscription subscription = getCurrentSubscription(token);
        if (subscription.getSubscriptionStatus() != SubscriptionStatus.ACTIVE){
            throw new EnterpriseException(ErrorType.NO_ACTIVE_SUBSCRIPTION);
        }
        return subscription.getSubscriptionPlan();
        }



    public Subscription getCurrentSubscription(String token) {
        Long userId = tokenToUserId(token);
        Optional<Subscription> optSubscription = subscriptionRepository
                .findTopByUserIdAndStatusAndEstimatedEndDateGreaterThanEqual(userId, EntityStatus.ACTIVE, System.currentTimeMillis());
        if (optSubscription.isPresent()) {
            return optSubscription.get();
        }
        else throw new EnterpriseException(ErrorType.NO_CURRENT_SUBSCRIPTION);
    }

    public List<Subscription> getSubscriptionHistory(String token) {
        Long userId = tokenToUserId(token);
        List<Subscription> subHistory = subscriptionRepository.findAllByUserIdAndStatus(userId, EntityStatus.ACTIVE);
        subHistory.sort((sub1, sub2)-> (int)(sub2.getEstimatedEndDate()-sub1.getEstimatedEndDate()));
        return subHistory;
    }

    public Subscription updateSubscriptionPlan(ChangeSubscriptionPlanRequest dto) {


        Subscription currentSubscription = getCurrentSubscription(dto.token());
        if (currentSubscription.getSubscriptionPlan().equals(dto.subscriptionPlan())) {
            throw new EnterpriseException(ErrorType.SAME_SUBSCRIPTION_PLAN);
        }
        int grade = dto.subscriptionPlan().compareTo(currentSubscription.getSubscriptionPlan());

        Subscription newSubscription = SubscriptionMapper.INSTANCE.subscriptionToSubscription(currentSubscription);
        newSubscription.setId(null);
        newSubscription.setSubscriptionPlan(dto.subscriptionPlan());
        newSubscription.setRelatedSubscriptionId(currentSubscription.getId());

        if (grade < 0) {
            newSubscription.setSubscriptionType(SubscriptionType.UPGRADED);
            currentSubscription.setSubscriptionType(SubscriptionType.UPGRADED_FROM);
        }
        else if (grade > 0) {
            newSubscription.setSubscriptionType(SubscriptionType.DOWNGRADED);
            currentSubscription.setSubscriptionType(SubscriptionType.DOWNGRADED_FROM);
        }

        newSubscription = subscriptionRepository.save(newSubscription);

        currentSubscription.setRelatedSubscriptionId(newSubscription.getId());
        currentSubscription.setEstimatedEndDate(System.currentTimeMillis());
        subscriptionRepository.save(currentSubscription);

        return newSubscription;
    }

    public Subscription cancelSubscription(String token) {
        Subscription subscription = getCurrentSubscription(token);
        if (subscription.getSubscriptionStatus() == SubscriptionStatus.CANCELLED)
            throw new EnterpriseException(ErrorType.ALREADY_CANCELLED);
        subscription.setSubscriptionStatus(SubscriptionStatus.CANCELLED);
        return subscriptionRepository.save(subscription);
    }
}


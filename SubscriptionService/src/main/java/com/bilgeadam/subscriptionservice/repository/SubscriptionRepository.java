package com.bilgeadam.subscriptionservice.repository;

import com.bilgeadam.subscriptionservice.entity.Subscription;
import com.bilgeadam.subscriptionservice.entity.enums.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {


    Optional<Subscription> findTopByUserIdAndEntityStatusAndEstimatedEndDateGreaterThanEqual(String userId, EntityStatus entityStatus, Long currentEpochMillis);

    List<Subscription> findAllByUserIdAndEntityStatus(String userId, EntityStatus entityStatus);

}

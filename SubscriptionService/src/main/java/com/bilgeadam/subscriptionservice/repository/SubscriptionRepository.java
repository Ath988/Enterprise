package com.bilgeadam.subscriptionservice.repository;

import com.bilgeadam.subscriptionservice.entity.Subscription;
import com.bilgeadam.subscriptionservice.entity.enums.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

    Subscription findByUserIdAndEntityStatus(String userId, EntityStatus entityStatus);
}

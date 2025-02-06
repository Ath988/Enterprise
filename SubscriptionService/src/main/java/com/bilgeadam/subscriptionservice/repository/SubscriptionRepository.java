package com.bilgeadam.subscriptionservice.repository;

import com.bilgeadam.subscriptionservice.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
}

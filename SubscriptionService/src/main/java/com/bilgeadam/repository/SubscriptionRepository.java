package com.bilgeadam.repository;

import com.bilgeadam.entity.Subscription;
import com.bilgeadam.entity.enums.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {


    Optional<Subscription> findTopByUserIdAndStatusAndEstimatedEndDateGreaterThanEqual(String userId, EntityStatus entityStatus, Long currentEpochMillis);

    List<Subscription> findAllByUserIdAndStatus(String userId, EntityStatus entityStatus);

}

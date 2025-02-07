package com.bilgeadam.repository;

import com.bilgeadam.entity.Subscription;
import com.bilgeadam.entity.enums.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {


    Optional<Subscription> findTopByUserIdAndEntityStatusAndEstimatedEndDateGreaterThanEqual(String userId, EntityStatus entityStatus, Long currentEpochMillis);

    List<Subscription> findAllByUserIdAndEntityStatus(String userId, EntityStatus entityStatus);

}

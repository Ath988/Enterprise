package com.bilgeadam.repository;

import com.bilgeadam.entity.Subscription;
import com.bilgeadam.entity.enums.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {


    Optional<Subscription> findTopByUserIdAndStatusAndEstimatedEndDateGreaterThanEqual(Long userId, EntityStatus entityStatus, Long currentEpochMillis);

    List<Subscription> findAllByUserIdAndStatus(Long userId, EntityStatus entityStatus);

    @Query("SELECT s.userId FROM Subscription s WHERE s.authId = ?1")
    Long findUserIdFromAuthId(Long authId);

}

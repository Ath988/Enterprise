package com.bilgeadam.subscriptionservice.entity;

import com.bilgeadam.subscriptionservice.entity.enums.SubscriptionStatus;
import com.bilgeadam.subscriptionservice.entity.enums.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Subscription")
@SuperBuilder
public class Subscription extends BaseEntity {

    String userId;
    @Enumerated(EnumType.STRING)
    SubscriptionPlan subscriptionPlan;
    Long startDate;
    Long estimatedEndDate;
    SubscriptionStatus subscriptionStatus;
    String relatedSubscriptionId;
}



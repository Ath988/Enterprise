package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.SubscriptionStatus;
import com.bilgeadam.entity.enums.SubscriptionType;
import com.bilgeadam.entity.enums.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Subscription")
@SuperBuilder
public class Subscription extends BaseEntity {

    Long userId;
    Long authId; //Token doğrulama için eklendi.
    @Enumerated(EnumType.STRING)
    SubscriptionPlan subscriptionPlan;
    Long startDate;
    Long estimatedEndDate;
    SubscriptionType subscriptionType;
    SubscriptionStatus subscriptionStatus;
    Long relatedSubscriptionId;
}



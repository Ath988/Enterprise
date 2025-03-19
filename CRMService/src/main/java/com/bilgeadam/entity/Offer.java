package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Embedded
	OfferDetail offerDetail;
	
	@Enumerated(EnumType.STRING)
	@Builder.Default
	OfferStatus offerStatus = OfferStatus.PENDING;
	
	Long customerId;
	
	@Builder.Default
	Boolean isAccepted = false;
}
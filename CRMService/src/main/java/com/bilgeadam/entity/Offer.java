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
	@Column(nullable = false)
	OfferStatus offerStatus = OfferStatus.PENDING;
	
	@Column(nullable = false)
	Long customerId;
	
	@Column(nullable = false)
	String customerName;
	
	@Column(nullable = false)
	String customerEmail;
	
	@Builder.Default
	Boolean isAccepted = false;
}
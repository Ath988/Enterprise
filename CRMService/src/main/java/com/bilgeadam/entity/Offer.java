package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "offers")
public class Offer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Embedded
	OfferDetail offerDetail;
	@Enumerated(EnumType.STRING)
	OfferStatus status;
	Long customerId;
}
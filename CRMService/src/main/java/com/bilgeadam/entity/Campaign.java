package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.ECampaign;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper=true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "campaigns")
public class Campaign extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name;
	String description;
	Double budget;
	LocalDateTime startDate;
	LocalDateTime endDate;
	Long customerId;
	@Enumerated(EnumType.STRING)
	@Builder.Default
	ECampaign status = ECampaign.PLANNED;
}
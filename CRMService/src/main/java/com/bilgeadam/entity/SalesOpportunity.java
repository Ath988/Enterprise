package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.ESalesOpportunity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "salesopportunities")
public class SalesOpportunity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String title;
	String description;
	Double estimatedValue;
	Long customerId;
	@Enumerated(EnumType.STRING)
	@Builder.Default
	ESalesOpportunity status = ESalesOpportunity.OPEN;
	Long campaignId;
}
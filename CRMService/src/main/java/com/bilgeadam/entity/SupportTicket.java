package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.ESupportTicket;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "supporttickets")
public class SupportTicket extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String subject;
	String description;
	Long customerId;
	@Enumerated(EnumType.STRING)
	@Builder.Default
	ESupportTicket status = ESupportTicket.OPEN;
}
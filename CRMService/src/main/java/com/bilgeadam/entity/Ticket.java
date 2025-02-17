package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.TicketPriority;
import com.bilgeadam.entity.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tickets")
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String subject;
	
	@Enumerated(EnumType.STRING)
	private TicketStatus status;
	
	@Enumerated(EnumType.STRING)
	private TicketPriority priority;
	
	private LocalDateTime createdAt;
	
	private Long customerId;
	
	@Transient // DB de tutma , sadece DTO için
	private List<TicketActivity> activities;
}
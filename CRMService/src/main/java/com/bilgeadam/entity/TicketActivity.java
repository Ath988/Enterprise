package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.ActivityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "ticket_activities")
public class TicketActivity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private ActivityType type;
	
	private LocalDateTime timestamp;
	
	@Embedded
	private ActivityPerformer performedBy;
	
	private String content;
	
	@Column(name = "ticket_id")
	private Long ticketId; // Ticket ID'sini doğrudan ID ile takip ediyoruz
}
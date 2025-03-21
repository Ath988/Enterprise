package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.ActivityType;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "ticket_activities")
public class TicketActivity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Enumerated(EnumType.STRING)
	ActivityType type;
	
	@Embedded
	ActivityPerformer performedBy;
	
	String content;
	
	Long ticketId;
}
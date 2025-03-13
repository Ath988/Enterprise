package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.TicketCategory;
import com.bilgeadam.entity.enums.TicketPriority;
import com.bilgeadam.entity.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tickets")
public class Ticket extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String subject;
	
	@Enumerated(EnumType.STRING)
	TicketStatus ticketStatus;
	
	@Enumerated(EnumType.STRING)
	TicketPriority priority;
	
	@Enumerated(EnumType.STRING)
	TicketCategory category;
	
	LocalDateTime closedAt; // Kapatılma tarihi
	
	@ElementCollection // Dosya yollarını tutan bir liste
	@CollectionTable(name = "ticket_attachments", joinColumns = @JoinColumn(name = "ticket_id"))
	@Column(name = "file_path")
	List<String> attachmentUrls;
	
	@Transient // DB de tutma , sadece DTO için
	List<TicketActivity> activities;
	
	Long customerId;
	
	Long performerId;
	
	@Column(unique = true, nullable = false, updatable = false)
	String ticketNumber; // UUID numarası
	
	@PrePersist
	public void generateTicketNumber() {
		this.ticketNumber = UUID.randomUUID().toString();
	}
}
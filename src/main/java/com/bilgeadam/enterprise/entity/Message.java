package com.bilgeadam.enterprise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tblmessage")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	@Column(nullable = false)
	private String content;
	@ManyToOne
	@JoinColumn(name = "sender_id")
	private User sender;
	@ManyToOne
	@JoinColumn(name = "chat_id", nullable = false)
	private Chat chat;
	@Column(nullable = false)
	@Builder.Default
	private LocalDateTime timeStamp=LocalDateTime.now();
	@Enumerated(EnumType.STRING)
	private EMessageStatus messageStatus;
}